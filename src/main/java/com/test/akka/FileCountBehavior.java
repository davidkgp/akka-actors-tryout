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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileCountBehavior extends AbstractBehavior<ICommand> {

    private FileCountBehavior(ActorContext<ICommand> context) {
        super(context);
    }

    public static Behavior<ICommand> create() {
        return Behaviors.setup(FileCountBehavior::new);
    }

    private int totalCount;

    private Set<ActorRef<ICommand>> spawnedActors = Collections.emptySet();


    @Override
    public Receive<ICommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(WordCountResponse.class, this::handleWordCountResponse)
                .onMessage(EOFMessage.class, this::handleEOF)
                .onMessage(StartCommand.class, this::handleStartCommand)
                .build();
    }

    private Behavior<ICommand> handleStartCommand(StartCommand startCommand) {

        List<String> listOfString = readAllLines(startCommand.getFileToBeCounted());
        for (int i = 0; i < listOfString.size(); i++) {
            String line = listOfString.get(i);

            if (line != null && line.trim().length() > 0) {
                ActorRef<ICommand> actorRef = getContext().spawn(WordCountBehavior.create(), "word-count-" + i);
                if (spawnedActors.isEmpty()) {
                    spawnedActors = new HashSet<>();
                }
                spawnedActors.add(actorRef);
                actorRef.tell(new WordCountCommand(listOfString.get(i), getContext().getSelf()));
            }


        }
        getContext().getSelf().tell(new EOFMessage());

        return Behaviors.same();
    }

    private Behavior<ICommand> handleWordCountResponse(WordCountResponse wordCountResponse) {
        totalCount = totalCount + wordCountResponse.getCount();
        if (!spawnedActors.isEmpty()) {
            spawnedActors.remove(wordCountResponse.getOwnRef());
        }
        return Behaviors.same();
    }

    private Behavior<ICommand> handleEOF(EOFMessage eofMessage) {
        if (spawnedActors.isEmpty()) {
            System.out.println("Total word Count " + totalCount);
            // perform graceful stop, executing cleanup before final system termination
            // behavior executing cleanup is passed as a parameter to Actor.stopped
            return Behaviors.stopped(() -> System.out.println("Parent Cleanup!"));
        } else {
            //so childs are not finished still need time to accumulate
            getContext().getSelf().tell(new EOFMessage());
        }
        return Behaviors.same();
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
