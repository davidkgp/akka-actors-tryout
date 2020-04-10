package com.test.akka;

import akka.actor.typed.ActorSystem;

import java.io.File;
import java.util.Objects;

public class Coordinator {

    public static void main(String[] args) {
        ActorSystem<File> fileActorSystem = ActorSystem.create(FileCountBehavior.create(), "file-count");
        File textFile = new File(Objects.requireNonNull(Coordinator.class.getClassLoader().getResource("some.txt")).getFile());
        fileActorSystem.tell(textFile);

    }
}
