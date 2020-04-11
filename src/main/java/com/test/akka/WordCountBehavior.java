package com.test.akka;


import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.test.akka.messages.ICommand;
import com.test.akka.messages.impl.WordCountCommand;
import com.test.akka.messages.impl.WordCountResponse;

public class WordCountBehavior extends AbstractBehavior<ICommand> {
    private WordCountBehavior(ActorContext<ICommand> context) {
        super(context);
    }

    public static Behavior<ICommand> create() {
        return Behaviors.setup(WordCountBehavior::new);
    }

    @Override
    public Receive<ICommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(WordCountCommand.class, wordCountCommand -> {
                    int count = wordCountCommand.getLine().split(" ").length;
                    wordCountCommand.getSender().tell(new WordCountResponse(count));
                    return this;
                }).build();
    }
}
