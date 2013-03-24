package org.ingini.akka.blocking.actor;

import akka.actor.UntypedActor;
import org.ingini.akka.blocking.message.AcknowledgementMessage;
import org.ingini.akka.blocking.message.BusinessMessage;
import org.ingini.akka.blocking.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;

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
public class BusinessConfirmationActor extends UntypedActor {

    @Autowired
    private BusinessService businessService;

    @Override
    public void onReceive(Object message) throws Exception {
        businessService.doBusiness((BusinessMessage) message);
        getSender().tell(AcknowledgementMessage.getInstance());
    }
}
