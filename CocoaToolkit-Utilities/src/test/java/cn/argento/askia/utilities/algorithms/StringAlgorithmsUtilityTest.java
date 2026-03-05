package cn.argento.askia.utilities.algorithms;

import org.junit.Test;

public class StringAlgorithmsUtilityTest {

    @Test
    public void testBF1(){
        char[] pattern = new char[]{'1', '0', '1', '1'};
        char[] text = "100110110111".toCharArray();
        int patternLength = pattern.length, i = 0;
        System.out.println("模式串长度（patternLength）：" + patternLength);
        int textLength = text.length, j = 0;
        System.out.println("文本串长度（textLength）：" + textLength);
        System.out.println("i = " + i + ", j = " + j);
        System.out.println("i < patternLength = " + (i < patternLength));
        System.out.println("j < textLength = " + (j < textLength));
        while(i < patternLength && j < textLength){
            System.out.println("pattern[i] == text[j] = " + (pattern[i] == text[j]));
            if (pattern[i] == text[j]){
                i++;
                j++;
                System.out.println("i = " + i + ", j = " + j);
            }
            else{
                // j回到最开始的位置，并且右移一位
                // 涉及到模式串坐标计算
                j -= (i - 1);
                // 模式串下标i从头开始
                i = 0;
                System.out.println("i = " + i + ", j = " + j);
            }
        }
        // 如果退出循环说明已经匹配了！
        // 则只需要返回最初的的文本串坐标就好！
        System.out.println("找到结果：" + (j - i));
    }
}
