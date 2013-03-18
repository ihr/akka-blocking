package org.ingini.akka.blocking.service;

import org.ingini.akka.blocking.message.BusinessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
@Service
public class BusinessService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doBusiness(BusinessMessage businessMessage) {
        logger.info("Processing business message {}", businessMessage.getMsgBody());
    }
}
