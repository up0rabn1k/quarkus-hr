package org.acme.messaging;

import io.quarkus.vertx.core.runtime.VertxCoreRecorder;
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle;
import io.smallrye.common.vertx.VertxContext;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.model.Customer;
import org.acme.persistence.CustomerEntity;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ReactiveMessagingConsumer {

    private final Mutiny.SessionFactory sessionFactory;

    @Incoming("customers-in")
    @Blocking(ordered = false)
    public Uni<Void> consume(Message<JsonObject> message) {
        final Executor executor = ReactiveMessagingConsumer::executeOnVertxContext;
        return Uni.createFrom()
                .item(message.getPayload().mapTo(Customer.class))
                .call(customer -> saveToDB(customer).runSubscriptionOn(executor))
                .onItem()
                .call(this::updateDB)
                .onItem()
                .transformToUni(item -> Uni.createFrom().completionStage(message.ack()));
    }

    public Uni<Void> saveToDB(Customer customer) {
        var customerEntity = new CustomerEntity();
        customerEntity.firstname = customer.firstName;
        customerEntity.lastName = customer.lastName;
        return sessionFactory.withStatelessSession(session -> session.insert(customerEntity).replaceWithVoid());
    }

    public Uni<Void> updateDB(Customer customer) {
        return sessionFactory.withStatelessSession(session ->
                session.createQuery("""
                                update EmployeeEntity e set e.name = :name where e.location = :location and e.department.id in (select d.id from DepartmentEntity d where d.id = :id)
                                """).setParameter("name", customer.firstName + " " + customer.lastName)
                        .setParameter("location", customer.location)
                        .setParameter("id", "1")
                        .executeUpdate()
                        .replaceWithVoid()
        );
    }

    public static void executeOnVertxContext(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Vertx vertx = VertxCoreRecorder.getVertx().get();
        var context = Vertx.currentContext();

        if (context == null) {
            context = vertx.getOrCreateContext();
        }
        if (!VertxContext.isDuplicatedContext(context)) {
            context = VertxContext.getOrCreateDuplicatedContext(context);
            VertxContextSafetyToggle.setContextSafe(context, true);
        }

        context.runOnContext(
                v -> {
                    try {
                        runnable.run();
                        completableFuture.complete(null);
                    } catch (Throwable t) {
                        completableFuture.completeExceptionally(t);
                    }
                });

        try {
            completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
        }
    }
}
