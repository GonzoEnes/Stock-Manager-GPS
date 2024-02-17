package gestorInventario.logic.db;

import java.util.Objects;

public class DBCondition {
    String booleanOperator;
    String field;
    String operator;
    Object value;

    public DBCondition(String booleanOperator, String field, String operator, Object value) {
        this.booleanOperator = booleanOperator;
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(booleanOperator)
                .append(" ")
                .append(field)
                .append(" ")
                .append(operator)
                .append(" '");

        if(value instanceof Boolean)
            sb.append((boolean) value ? 1 : 0);
        else
            sb.append(value);

        sb.append("' ");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBCondition that = (DBCondition) o;
        return booleanOperator.equals(that.booleanOperator) && field.equals(that.field) && operator.equals(that.operator) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(booleanOperator, field, operator, value);
    }
}
