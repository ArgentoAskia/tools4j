package cn.argento.askia.utilities.algorithms;

import cn.argento.askia.annotations.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Utility(value = "字符串相关算法工具类")
public class StringAlgorithmsUtility {

    private StringAlgorithmsUtility(){
        throw new IllegalAccessError("StringAlgorithmsUtility为工具类, 无法创建该类的对象");
    }

//    public static void main(String[] args) {
//        final int i = BF1("glk".toCharArray(), "agidgcjhggik".toCharArray());
//        System.out.println(i);
//
//        // 1 1 2 4 3 3 7 6 3
//        final int[] ints = buildNext("abcabdabe".toCharArray());
//        System.out.println(Arrays.toString(ints));
//    }

    public static void main(String[] args) {
        String file1Path = "C:\\Users\\Admin\\Desktop\\1.txt";
        String file2Path = "C:\\Users\\Admin\\Desktop\\2.txt";
        String mergedFilePath = "C:\\Users\\Admin\\Desktop\\merged.txt";

        try {
            // 读取两个文件的内容
            List<String> lines1 = Files.readAllLines(Paths.get(file1Path));
            List<String> lines2 = Files.readAllLines(Paths.get(file2Path));

            // 按行合并两个列表
            List<String> mergedLines = IntStream.range(0, Math.max(lines1.size(), lines2.size()))
                    .mapToObj(i -> {
                        String line1 = i < lines1.size() ? lines1.get(i) : "";
                        String line2 = i < lines2.size() ? lines2.get(i) : "";
                        return line1 + "\\" + "n" + line2; // 按需调整合并逻辑
                    })
                    .collect(Collectors.toList());

            // 将合并后的内容写入新文件
            Files.write(Paths.get(mergedFilePath), mergedLines);

            System.out.println("文件合并完成，结果已保存到：" + mergedFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * BF模式匹配算法.
     *
     * <p>也称暴力算法, 用于匹配模式串出现在文本串中的位置</p>
     * <p>此为邓俊辉《数据结构C++描述》中的第一个版本</p>
     * @param pattern 模式串
     * @param text 文本串
     * @return 出现的位置
     */
    public static int BF1(char[] pattern, char[] text){
        int patternLength = pattern.length, i = 0;
        int textLength = text.length, j = 0;
        while(i < patternLength && j < textLength){
            if (pattern[i] == text[j]){
                i++;
                j++;
            }
            else{
                // j回到最开始的位置，并且右移一位
                // 涉及到模式串坐标计算
                j -= (i - 1);
                // 模式串下标i从头开始
                i = 0;
            }
        }
        // 如果退出循环说明已经匹配了！
        // 则只需要返回最初的的文本串坐标就好！
        return j - i;
    }

    /**
     * BF模式匹配算法.
     *
     * <p>也称暴力算法, 用于匹配模式串出现在文本串中的位置</p>
     * <p>此为邓俊辉《数据结构C++描述》中的第二个版本</p>
     * @param pattern 模式串
     * @param text 文本串
     * @return 出现的位置
     */
    public static int BF2(char[] pattern, char[] text){
        int patternLength = pattern.length, i = 0;
        int textLength = text.length, j = 0;
        for (j = 0; i < textLength - patternLength + 1; j++){
            for (i = 0; i < patternLength; i++) {
                // 失配则break
                if (text[j + i] == pattern[i])  break;
            }
            // 成功
            if (i >= patternLength){
                break;
            }
        }
        return j;
    }

    /**
     * KMP模式匹配算法.
     *
     * <p>代码来自邓俊辉《数据结构C++描述》，有部分修改</p>
     * @param pattern 模式串
     * @param text 文本串
     * @return 出现的位置
     */
    public static int KMP(char[] pattern, char[] text){
        // 创建 next 表
        int[] nextTable = buildNext(pattern);
        // 获取字符串长度
        int n = text.length;
        int m = pattern.length;
        int i = 0, j = 0;
        while (j < m && i < n){
            // 如果j < 0 或者模式串和匹配串相同，则进位
            if (j < 0 || text[i] == pattern[j]){
                i++;j++;
            }
            // 否额移动到合适位置继续比较
            else{
                j = nextTable[j];
            }
        }
        // 找到则进行相减即可得到位置！
        return i - j;
    }




    // next 字符表无非下面的情况
    // C A B C ,第一位错，移动全部， 第二位、第三位错，但它们还有可能是c，所以对准C，第四位错，移动全部
    // C C C D，第一、二、三位错，移动全部，第四位错，但它有可能是C，所以让最后一个C对准它进行判断！
    // C C C C 移动全部！
    // C A B C X? 如果第5位失配，则需要考虑第五位是否是A，所以让A对齐X的位置，如果存在多个，则选则最靠右的那个，比如
    // C A B C A C X? 选择最右边的CA


    // 口算next表的方法：-1代表模式串完全右移一位，0代表模式串第一位与当前失配位置进行匹配
    // 第一位永远都是-1
    // 第二位如果和第一位相同，则同样-1，如果不同，因为有可能是第一位的字符，所以匹配第一位
    // 第三位同样如果和第一位相同，则是-1，否则则看第二位是否和第一位相同，如果相同，则让第二位对齐，否则则让第一位对齐
    // 以此类推！
    // 如果当前位的前一位等于第一位，则当前位如果第二第二位，则和第二位对齐
    private static int[] buildNext(char[] pattern) {
        int m = pattern.length;
        int j = 0;
        int[] next = new int[m];
        int t = next[0] = -1;
        // 双指针寻找相同字串
        while(j < m - 1){
            if (t < 0 || pattern[t] == pattern[j]){
                j++;
                t++;
//                next[j] = t;
                // 优化...
                next[j] = (pattern[t] != pattern[j]? t : next[t]);
            }
            // 出现失配
            // 则需要回退t
            else{
                t = next[t];
            }
        }
        return next;
    }
}
