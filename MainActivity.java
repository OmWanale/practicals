package com.example.calculator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String cur = "";
    double a = 0;
    String op = "";

    String[] keys = {
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "="
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF121212);
        root.setFitsSystemWindows(true);
        root.setPadding(24, 24, 24, 24);

        tv = new TextView(this);
        tv.setText("0");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(56);
        tv.setGravity(Gravity.BOTTOM | Gravity.END);
        root.addView(tv, new LinearLayout.LayoutParams(-1, 0, 1.0f));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(5);
        root.addView(grid, new LinearLayout.LayoutParams(-1, 0, 2.0f));

        for (String k : keys) {
            Button btn = new Button(this);
            btn.setText(k);
            btn.setTextSize(26);

            boolean isTop = "C ⌫ %".contains(k);
            boolean isOrange = "+-×÷=".contains(k);

            btn.setTextColor(isTop ? Color.BLACK : Color.WHITE);
            btn.setBackgroundTintList(ColorStateList.valueOf(
                    isOrange ? 0xFFFF9F0A : isTop ? 0xFFA5A5A5 : 0xFF333333));

            int span = k.equals("0") ? 2 : 1;
            GridLayout.LayoutParams p = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1, 1.0f),
                    GridLayout.spec(GridLayout.UNDEFINED, span, (float) span)
            );
            p.width = 0;
            p.height = 0;
            p.setMargins(6, 6, 6, 6);
            grid.addView(btn, p);

            btn.setOnClickListener(v -> press(k));
        }

        setContentView(root);
    }

    void press(String t) {
        if (t.matches("[0-9]")) {
            cur += t;
        } else if (t.equals(".") && !cur.contains(".")) {
            cur += cur.isEmpty() ? "0." : ".";
        } else if (t.equals("C")) {
            cur = "";
            op = "";
            a = 0;
        } else if (t.equals("⌫")) {
            if (!cur.isEmpty()) {
                cur = cur.substring(0, cur.length() - 1);
            }
        } else if (t.equals("=")) {
            if (!op.isEmpty() && !cur.isEmpty()) {
                cur = fmt(calc(a, Double.parseDouble(cur), op));
                op = "";
            }
        } else if ("+-×÷%".contains(t) && !cur.isEmpty()) {
            if (!op.isEmpty()) {
                cur = fmt(calc(a, Double.parseDouble(cur), op));
            }
            a = Double.parseDouble(cur);
            op = t;
            cur = "";
        }

        if (cur.isEmpty()) {
            tv.setText(op.isEmpty() ? "0" : fmt(a) + " " + op);
        } else {
            tv.setText(op.isEmpty() ? cur : fmt(a) + " " + op + " " + cur);
        }
    }

    double calc(double x, double y, String o) {
        switch (o) {
            case "+": return x + y;
            case "-": return x - y;
            case "×": return x * y;
            case "÷": return y == 0 ? Double.NaN : x / y;
            default:  return x % y;
        }
    }

    String fmt(double d) {
        if (Double.isNaN(d)) return "Error";
        return d == (long) d ? String.valueOf((long) d) : String.valueOf(d);
    }
}