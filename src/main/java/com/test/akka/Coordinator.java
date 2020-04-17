package com.test.akka;

import akka.actor.typed.ActorSystem;
import com.test.akka.messages.ICommand;
import com.test.akka.messages.impl.StartCommand;

import java.io.File;
import java.util.Objects;

public class Coordinator {

    public static void main(String[] args) {


        ActorSystem<ICommand> fileActorSystem = ActorSystem.create(FileCountBehavior.create(), "file-count");
        File textFile = new File(Objects.requireNonNull(Coordinator.class.getClassLoader().getResource("some.txt")).getFile());
        fileActorSystem.tell(new StartCommand(textFile));

    }
}
