package com.craftinginterpreters.lox;

public class Interpreter implements Expr.Visitor<Object> {
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
                return (double)left > (double)right;
            case TokenType.GREATER_EQUAL:
                return (double)left >= (double)right;
            case TokenType.LESS:
                return (double)left < (double)right;
            case TokenType.LESS_EQUAL:
                return (double)left <= (double)right;
            case TokenType.BANG_EQUAL:
                return !isEqual(left, right);
            case TokenType.EQUAL_EQUAL:
                return isEqual(left, right);
            case TokenType.MINUS:
                return (double)left - (double)right;
            case TokenType.SLASH:
                return (double)left / (double)right;
            case TokenType.STAR:
                return (double)left * (double)right;
            case TokenType.PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }

                break;
            default:
                return null;
        }

        return null;
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
}
