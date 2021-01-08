package cn.bobdeng.domainevent;


import domainevent.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static cn.bobdeng.testtools.SnapshotMatcher.snapshotMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = {TestApplication.class})
class EventPublisherSpringLocalTest {
    @Autowired
    TestListenNoConditionController testController;
    @Autowired
    TestListenWithConditionController testListenWithConditionController;
    @Autowired
    EventPublisher eventPublisher;
    @BeforeEach
    public void setup(){
        testController.clear();
        testListenWithConditionController.clear();
    }
    @Test
    public void Given注册了无条件事件_When发布事件_Then方法被调用() {
        eventPublisher.publish(new TestEvent(false));
        assertThat(testController.getEvents(), snapshotMatch(this, "events_no_condition"));
    }

    @Test
    public void Given注册了有条件事件_When条件不符合_Then方法没有被调用() {
        eventPublisher.publish(new TestEvent(false));
        assertThat(testListenWithConditionController.getEvents().size(),is(0));
    }

    @Test
    public void Given注册了有条件事件_When条件符合_Then方法被调用() {
        eventPublisher.publish(new TestEvent(true));
        assertThat(testController.getEvents(), snapshotMatch(this, "events_has_condition"));

    }
}
