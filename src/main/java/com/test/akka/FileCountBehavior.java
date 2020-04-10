package com.test.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileCountBehavior extends AbstractBehavior<File> {

    private FileCountBehavior(ActorContext<File> context) {
        super(context);
    }

    public static Behavior<File> create() {
        return Behaviors.setup(FileCountBehavior::new);
    }


    @Override
    public Receive<File> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(file -> {
                    List<String> listOfString = readAllLines(file);
                    for (int i = 0; i < listOfString.size(); i++) {
                        String line = listOfString.get(i);

                        if(line!=null && line.trim().length()>0) {
                            ActorRef<String> actorRef = getContext().spawn(WordCountBehavior.create(), "word-count-" + i);
                            actorRef.tell(listOfString.get(i));
                        }
                    }
                    return this;
                }).build();
    }

    private List<String> readAllLines(File file) {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
