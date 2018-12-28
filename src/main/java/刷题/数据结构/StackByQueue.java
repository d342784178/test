package 刷题.数据结构;

import org.junit.Assert;

/**
 * Desc: 两个队列实现栈
 * 1. in/out队列
 * 2. in入队 out出队
 * 3.
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 20:08
 * https://img-blog.csdn.net/20150902105044136
 */
public class StackByQueue implements StackI<Integer> {
    private Queue<Integer> in  = new Queue<>();
    private Queue<Integer> out = new Queue<>();

    @Override
    public Integer pop() {
        if (out.size() >= 1) {
            Integer t = null;
            while (out.peek() != null && out.size() > 1) {
                t = out.pop();
                in.push(t);
            }
            return out.pop();
        } else if (in.size() >= 1) {
            Integer t = null;
            while (in.peek() != null && in.size() > 1) {
                t = in.pop();
                out.push(t);
            }
            Integer        pop  = in.pop();
            Queue<Integer> temp = out;
            out = in;
            in = temp;
            return pop;
        } else {
            return null;
        }
    }

    @Override
    public void push(Integer integer) {
        out.push(integer);
    }

    @Override
    public boolean isEmpty() {
        return in.isEmpty() && out.isEmpty();
    }

    @Override
    public Integer peek() {
        Integer pop = pop();
        push(pop);
        return pop;
    }

    @Override
    public int size() {
        return in.size() + out.size();
    }

    public static void main(String args[]) {
        StackByQueue stack = new StackByQueue();
        stack.push(1);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        Assert.assertTrue(stack.pop() == 3);
        stack.push(4);
        stack.push(5);
        Assert.assertTrue(stack.pop() == 5);
        Assert.assertTrue(stack.pop() == 4);
        Assert.assertTrue(stack.pop() == 2);
        Assert.assertTrue(stack.pop() == 1);
        Assert.assertTrue(stack.isEmpty() == true);
    }
}
