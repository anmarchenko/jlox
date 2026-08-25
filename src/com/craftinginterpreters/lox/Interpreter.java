package com.craftinginterpreters.lox;

public class Interpreter implements Expr.Visitor<Object> {
    public void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case TokenType.MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            case TokenType.BANG:
                return !isTruthy(right);
            default:
                return null;
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case TokenType.GREATER:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left > (double)right;
            case TokenType.GREATER_EQUAL:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left >= (double)right;
            case TokenType.LESS:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left < (double)right;
            case TokenType.LESS_EQUAL:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left <= (double)right;
            case TokenType.BANG_EQUAL:
                return !isEqual(left, right);
            case TokenType.EQUAL_EQUAL:
                return isEqual(left, right);
            case TokenType.MINUS:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left - (double)right;
            case TokenType.SLASH:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left / (double)right;
            case TokenType.STAR:
                checkNumberOperand(expr.operator, left);
                checkNumberOperand(expr.operator, right);
                return (double)left * (double)right;
            case TokenType.PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }

                if (left instanceof String) {
                    return (String)left + stringify(right);
                }

                throw new RuntimeError(expr.operator, "Expected numbers or strings");
            default:
                return null;
        }
    }

    public Object visitConditionalExpr(Expr.Conditional expr) {
        boolean condition = (boolean)evaluate(expr.condition);

        if (condition) {
            return evaluate(expr.thenBranch);
        } else {
            return evaluate(expr.elseBranch);
        }
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;

        return left.equals(right);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Expected number, got " + operand);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}
