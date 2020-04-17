package com.test.akka;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import com.test.akka.messages.ICommand;
import com.test.akka.messages.impl.WordCountCommand;
import com.test.akka.messages.impl.WordCountResponse;
import org.junit.Test;

public class WordCountBehaviorTest {

    @Test
    public void testActorsResponseToEmptyString() {
        BehaviorTestKit<ICommand> spawnWordCountActor = BehaviorTestKit.create(WordCountBehavior.create());
        TestInbox<ICommand> dummyReceiver = TestInbox.create();
        spawnWordCountActor.run(new WordCountCommand("", dummyReceiver.getRef()));
        dummyReceiver.expectMessage(new WordCountResponse(0, spawnWordCountActor.getRef()));

    }

    @Test
    public void testActorsResponseToNullString() {
        BehaviorTestKit<ICommand> spawnWordCountActor = BehaviorTestKit.create(WordCountBehavior.create());
        TestInbox<ICommand> dummyReceiver = TestInbox.create();
        spawnWordCountActor.run(new WordCountCommand(null, dummyReceiver.getRef()));
        dummyReceiver.expectMessage(new WordCountResponse(0, spawnWordCountActor.getRef()));

    }

    @Test
    public void testActorsResponseToNonEmptyString() {
        BehaviorTestKit<ICommand> spawnWordCountActor = BehaviorTestKit.create(WordCountBehavior.create());
        TestInbox<ICommand> dummyReceiver = TestInbox.create();
        spawnWordCountActor.run(new WordCountCommand("Hello How are you?", dummyReceiver.getRef()));
        dummyReceiver.expectMessage(new WordCountResponse(4, spawnWordCountActor.getRef()));

    }
}
