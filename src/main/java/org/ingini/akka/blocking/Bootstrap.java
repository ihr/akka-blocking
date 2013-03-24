package org.ingini.akka.blocking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.ingini.akka.blocking.actor.FireAndAwaitMediatorActor;
import org.ingini.akka.di.DependencyInjectionProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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
@Configuration
@ComponentScan({"org.ingini.akka.blocking.service"})
public class Bootstrap {

    public static final String FIRE_AND_AWAIT_ROUTER = "fire-and-await-router";
    public static final String ACTOR_SYSTEM = "ingini-fire-and-await-actor-system";

    private ActorSystem actorSystem;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = ACTOR_SYSTEM, destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        actorSystem = ActorSystem.create(ACTOR_SYSTEM);
        return actorSystem;
    }

    @Bean(name = FIRE_AND_AWAIT_ROUTER)
    @DependsOn({ACTOR_SYSTEM})
    public ActorRef fireAndAwaitRouter() {
        return actorSystem.actorOf(//
                new DependencyInjectionProps(applicationContext, FireAndAwaitMediatorActor.class), FIRE_AND_AWAIT_ROUTER);
    }

}
