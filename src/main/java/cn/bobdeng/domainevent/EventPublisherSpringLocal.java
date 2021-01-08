package cn.bobdeng.domainevent;

import domainevent.DomainEvent;
import domainevent.DomainEventService;
import domainevent.EventPublisher;
import domainevent.ExpressionEval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class EventPublisherSpringLocal implements EventPublisher, ApplicationListener<ApplicationContextEvent> {
    private final ConfigurableListableBeanFactory factory;
    private final DomainEventService domainEventService;

    public EventPublisherSpringLocal(ConfigurableListableBeanFactory factory, ExpressionEval expressionEval) {
        this.factory = factory;
        this.domainEventService = new DomainEventService(this, expressionEval);
    }

    @Override
    public void publish(Object event) {
        domainEventService.onMessageObject(event);
    }

    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        Stream.of(applicationContextEvent.getApplicationContext().getBeanDefinitionNames())
                .map(this::getBeanDefinition)
                .filter(Objects::nonNull)
                .forEach(this::registerBeanWithDomainEventMethods);
    }

    private BeanDefinition getBeanDefinition(String name) {
        try {
            return factory.getBeanDefinition(name);
        } catch (Exception e) {
            return null;
        }
    }

    private void registerBeanWithDomainEventMethods(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName == null) {
            return;
        }
        registerBeanIfClassHasDomainEventMethods(beanClassName);
    }

    private void registerBeanIfClassHasDomainEventMethods(String beanClassName) {
        try {
            Class clz = Class.forName(beanClassName);
            if (isBeanHasMethodWithDomainEvent(clz)) {
                registerBean(clz);
            }
        } catch (ClassNotFoundException e) {
            log.warn("", e);
        }
    }

    private boolean isBeanHasMethodWithDomainEvent(Class clz) {
        return Stream.of(clz.getMethods())
                .anyMatch(method -> method.getDeclaredAnnotation(DomainEvent.class) != null);
    }

    private void registerBean(Class clz) {
        Object bean = factory.getBean(clz);
        domainEventService.registerSubscriber(bean, clz);
    }
}
