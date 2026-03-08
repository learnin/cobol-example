package cobol.sample1;

/**
 * COPY "COPY1" REPLACING LEADING ==FOO== BY ==BAR== の展開: 01 COPY-WORK-AREA2。
 *
 * COBOL 元定義（copy1.cpy）では FOO-STR1/2/3 だが、
 * REPLACING 句により BAR-STR1/2/3 に置換されている。
 *
 * COBOL 元定義（copy1.cpy）:
 *   01 COPY-WORK-AREA2.
 *     03 FOO-STR1 PIC X(10).   → BAR-STR1
 *     03 FOO-STR2 PIC X(10).   → BAR-STR2
 *     03 FOO-STR3 PIC X(10).   → BAR-STR3
 */
public class CopyWorkArea2 {

    // 03 BAR-STR1 PIC X(10)  （元: FOO-STR1）
    private String barStr1 = "          ";
    // 03 BAR-STR2 PIC X(10)  （元: FOO-STR2）
    private String barStr2 = "          ";
    // 03 BAR-STR3 PIC X(10)  （元: FOO-STR3）
    private String barStr3 = "          ";

    public String getBarStr1() { return barStr1; }
    public void   setBarStr1(String v) { barStr1 = CobolUtil.move(v, 10); }

    public String getBarStr2() { return barStr2; }
    public void   setBarStr2(String v) { barStr2 = CobolUtil.move(v, 10); }

    public String getBarStr3() { return barStr3; }
    public void   setBarStr3(String v) { barStr3 = CobolUtil.move(v, 10); }
}
