package org.acme.messaging;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.acme.model.Customer;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;

@Slf4j
@ApplicationScoped
public class ReactiveMessagingProducer {

    @Outgoing("customers-out")
    public Multi<Message<JsonObject>> produce() {
        return Multi.createFrom()
                .ticks()
                .every(Duration.ofMillis(1))
                .map(tick -> {

                            var customer = new Customer();
                            customer.firstName = "firstName" + tick;
                            customer.lastName = "lastName" + tick;
                            customer.location = (tick % 2 == 0) ? "GB" : "US";

                            return (Message<JsonObject>) () -> JsonObject.mapFrom(customer);
                        }
                )
                .select()
                .first(Duration.ofSeconds(10000000))
                .onOverflow()
                .buffer(5000);
    }
}
