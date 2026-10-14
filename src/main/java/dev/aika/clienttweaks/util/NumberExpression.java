package dev.aika.clienttweaks.util;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberExpression {
    private static final Pattern CONDITION = Pattern.compile(
            "(>=|<=|>|<|=)?\\s*([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?)"
    );

    private final List<Condition> conditions;
    @Getter
    private final boolean any;

    private NumberExpression(List<Condition> conditions, boolean any) {
        this.conditions = List.copyOf(conditions);
        this.any = any;
    }

    public static NumberExpression parse(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }

        String input = expression.trim();

        if (input.equals("*")) {
            return new NumberExpression(List.of(), true);
        }

        if (input.contains("*")) {
            throw new IllegalArgumentException("'*' cannot be combined with other conditions");
        }

        List<Condition> conditions = new ArrayList<>();
        Matcher matcher = CONDITION.matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            if (!input.substring(lastEnd, matcher.start()).isBlank()) {
                throw new IllegalArgumentException(
                        "Invalid condition: " + input.substring(lastEnd, matcher.start())
                );
            }

            String operator = matcher.group(1);
            String number = matcher.group(2);

            Operator op = Operator.fromSymbol(operator);
            conditions.add(new Condition(op, new BigDecimal(number)));
            lastEnd = matcher.end();
        }

        if (!input.substring(lastEnd).isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid condition: " + input.substring(lastEnd)
            );
        }

        if (conditions.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }

        return new NumberExpression(conditions, false);
    }

    public boolean matches(double value) {
        if (!Double.isFinite(value)) return false;

        return matches(BigDecimal.valueOf(value));
    }

    public boolean matches(BigDecimal value) {
        if (value == null) return false;

        if (any) return true;

        for (Condition condition : conditions) {
            int comparison = value.compareTo(condition.value());

            boolean matched = switch (condition.operator()) {
                case GREATER -> comparison > 0;
                case GREATER_OR_EQUAL -> comparison >= 0;
                case LESS -> comparison < 0;
                case LESS_OR_EQUAL -> comparison <= 0;
                case EQUAL -> comparison == 0;
            };

            if (!matched) return false;
        }
        return true;
    }

    @Override public String toString() {
        if (any) return "*";

        final StringBuilder result = new StringBuilder();

        for (Condition condition : conditions) {
            if (!result.isEmpty()) result.append(' ');

            result.append(condition.operator().symbol())
                    .append(condition.value().stripTrailingZeros().toPlainString());
        }
        return result.toString();
    }

    private enum Operator {
        GREATER(">"),
        GREATER_OR_EQUAL(">="),
        LESS("<"),
        LESS_OR_EQUAL("<="),
        EQUAL("=");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String symbol() {
            return symbol;
        }

        public static Operator fromSymbol(String symbol) {
            String s = symbol == null ? "=" : symbol;

            for (Operator op : values()) {
                if (op.symbol.equals(s)) {
                    return op;
                }
            }

            throw new IllegalArgumentException("Invalid operator: " + symbol);
        }
    }

    private record Condition(Operator operator, BigDecimal value) {
    }
}