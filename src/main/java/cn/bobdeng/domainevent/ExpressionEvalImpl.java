package cn.bobdeng.domainevent;

import domainevent.ExpressionEval;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpressionEvalImpl implements ExpressionEval {
    @Override
    public boolean when(String when, Object event) {
        StandardEvaluationContext simpleContext = new StandardEvaluationContext(event);
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(when);
        return Optional.ofNullable(exp.getValue(simpleContext, Boolean.class)).orElse(false);
    }
}
