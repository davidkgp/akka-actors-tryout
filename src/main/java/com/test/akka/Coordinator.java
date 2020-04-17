package com.test.akka;

import akka.actor.typed.ActorSystem;
import com.test.akka.messages.ICommand;
import com.test.akka.messages.impl.StartCommand;

import java.io.File;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static akka.actor.typed.javadsl.AskPattern.ask;

public class Coordinator {

    public static void main(String[] args) {


        ActorSystem<ICommand> fileActorSystem = ActorSystem.create(FileCountBehavior.create(), "file-count");
        File textFile = new File(Objects.requireNonNull(Coordinator.class.getClassLoader().getResource("some.txt")).getFile());
        CompletionStage<Integer> returnedResult = ask(fileActorSystem, me -> new StartCommand(textFile, me), Duration.ofSeconds(10), fileActorSystem.scheduler());

        returnedResult.whenComplete((intValue, error) -> {
            if (error != null) {
                error.printStackTrace();
            }

            System.out.println("Total Count " + intValue);
        });


    }
}
