package cn.bobdeng.domainevent;

import domainevent.DomainEvent;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class TestListenNoConditionController {
    private List<Object> events = new ArrayList<>();

    public void clear() {
        events.clear();
    }

    @DomainEvent
    public void onTestEventNoCondition(TestEvent event) {
        this.events.add(event);
    }
}
