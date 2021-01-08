package cn.bobdeng.domainevent;

import lombok.Data;

@Data
public class TestEvent {
    private boolean flag;

    public TestEvent(boolean flag) {
        this.flag = flag;
    }
}
