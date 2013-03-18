package org.ingini.akka.blocking;

import akka.actor.ActorRef;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import org.ingini.akka.blocking.message.BusinessMessage;
import org.ingini.akka.blocking.message.BusinessMessagesHolder;
import org.ingini.akka.blocking.service.BusinessService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

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
@ContextConfiguration(classes = {Bootstrap.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class BusinessTest {

    @Autowired
    private ActorRef fireAndAwaitRouter;

    @Rule
    public ExpectedException expectedExceptionRule = ExpectedException.none();

    @Mock
    public BusinessService mockedBusinessService;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void allActorShouldAcknowledgeWithinTime() throws Exception {
        //GIVEN
        List<BusinessMessage> messages = new ArrayList<BusinessMessage>(100);
        for (int i = 0; i < 100; i++) {
            messages.add(new BusinessMessage("Msg number: " + i));
        }

        Duration duration = Duration.apply("10 sec");

        //WHEN
        Future<Object> answer = Patterns.ask(fireAndAwaitRouter, new BusinessMessagesHolder(messages), //
                Timeout.durationToTimeout(duration));

        //THEN
        Await.result(answer, duration);

    }

    @Test
    public void failureToAcknowledgeWithinTimeout() throws Exception {
        //GIVEN
        expectedExceptionRule.expect(TimeoutException.class);
        expectedExceptionRule.expectMessage("Futures timed out after [1] milliseconds");

        List<BusinessMessage> messages = new ArrayList<BusinessMessage>(100000);
        for (int i = 0; i < 100; i++) {
            messages.add(new BusinessMessage("Msg number: " + i));
        }

        Duration duration = Duration.apply("1 millisecond");

        //WHEN
        Future<Object> answer = Patterns.ask(fireAndAwaitRouter, new BusinessMessagesHolder(messages), //
                Timeout.durationToTimeout(duration));

        //THEN
        Await.result(answer, duration);

    }
}
