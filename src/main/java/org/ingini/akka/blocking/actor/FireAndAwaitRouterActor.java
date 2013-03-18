package org.ingini.akka.blocking.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import org.ingini.akka.blocking.message.AcknowledgementMessage;
import org.ingini.akka.blocking.message.BusinessMessagesHolder;
import org.ingini.akka.di.DependencyInjectionProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Copyright (c) 2013 Ivan Hristov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FireAndAwaitRouterActor extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_NUMBER_OF_ACTORS = 5;
    private ActorRef router;

    @Autowired
    private ApplicationContext applicationContext;

    private long messageCounter;
    private ActorRef acknowledgeRequester;

    @Override
    public void preStart() {
        router = getContext().actorOf(new DependencyInjectionProps(applicationContext, BusinessConfirmationActor.class)//
                .withRouter(new RoundRobinRouter(MAX_NUMBER_OF_ACTORS)), "router");
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof BusinessMessagesHolder) {
            acknowledgeRequester = getSender();
            List messages = ((BusinessMessagesHolder) message).getBusinessMessages();
            messageCounter = messages.size();
            processBusinessMessages(messages);
            return;
        } else if (message instanceof AcknowledgementMessage) {
            processAcknowledgementMessage(message);
            return;
        }

        logger.error("Cannot process message: {}", message);

        throw new IllegalStateException("Cannot process message!");
    }

    private void processAcknowledgementMessage(Object message) {
        messageCounter--;
        if (messageCounter < 1 && acknowledgeRequester != null) {
            logger.info("Finally all messages are processed! Notifying requester ... ");
            acknowledgeRequester.tell(message);
        } else {
            logger.info("Messages are processed but no acknowledge requester is set. Awaiting for acknowledgement request.");
        }
    }

    private void processBusinessMessages(List messages) {
        logger.info("Total message for processing: {}", messageCounter);
        for (Object msg : messages) {
            router.tell(msg, getSelf());
        }
    }

}
