package com.test.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.test.akka.messages.ICommand;
import com.test.akka.messages.impl.EOFMessage;
import com.test.akka.messages.impl.StartCommand;
import com.test.akka.messages.impl.WordCountCommand;
import com.test.akka.messages.impl.WordCountResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileCountBehavior extends AbstractBehavior<ICommand> {

    private FileCountBehavior(ActorContext<ICommand> context) {
        super(context);
    }

    public static Behavior<ICommand> create() {
        return Behaviors.setup(FileCountBehavior::new);
    }

    private int totalCount;


    @Override
    public Receive<ICommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(WordCountResponse.class, wordCountResponse -> {
                    totalCount = totalCount+wordCountResponse.getCount();
                    return this;
                })
                .onMessage(EOFMessage.class, eofMessage -> {
                    System.out.println("Total word Count " + totalCount);
                    return this;
                })
                .onMessage(StartCommand.class, startCommand -> {

                    List<String> listOfString = readAllLines(startCommand.getFileToBeCounted());
                    for (int i = 0; i < listOfString.size(); i++) {
                        String line = listOfString.get(i);

                        if (line != null && line.trim().length() > 0) {
                            ActorRef<ICommand> actorRef = getContext().spawn(WordCountBehavior.create(), "word-count-" + i);
                            actorRef.tell(new WordCountCommand(listOfString.get(i), getContext().getSelf()));
                        }



                    }
                    getContext().getSelf().tell(new EOFMessage());

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
