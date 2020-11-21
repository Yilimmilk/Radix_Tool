package com.icezx.radix;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * @author Yili <miaococoo@gmail.com>
 * @date 2019/07/07
 */

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    //声明所有组件
    private RadioButton rdb2, rdb8, rdb10, rdb16;
    private EditText etText, et2, et8, et10, et16;
    private Button btDel;
    private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, bta, btb, btc, btd, bte, btf, btPoint;

    private SharedPreferences.Editor editor;

    private int select = 10;
    private int digit = 5;
    private int colorAccentInt;

    private String BUILDTIME = BuildConfig.BUILD_TIME;

    //OnCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorAccentInt = getColor(R.color.colorAccent);

        rdb2 = findViewById(R.id.rdb_2);
        rdb8 = findViewById(R.id.rdb_8);
        rdb10 = findViewById(R.id.rdb_10);
        rdb16 = findViewById(R.id.rdb_16);
        etText = findViewById(R.id.et_text);
        et2 = findViewById(R.id.et_2);
        et8 = findViewById(R.id.et_8);
        et10 = findViewById(R.id.et_10);
        et16 = findViewById(R.id.et_16);
        bt1 = findViewById(R.id.bt_1);
        bt2 = findViewById(R.id.bt_2);
        bt3 = findViewById(R.id.bt_3);
        bt4 = findViewById(R.id.bt_4);
        bt5 = findViewById(R.id.bt_5);
        bt6 = findViewById(R.id.bt_6);
        bt7 = findViewById(R.id.bt_7);
        bt8 = findViewById(R.id.bt_8);
        bt9 = findViewById(R.id.bt_9);
        bt0 = findViewById(R.id.bt_0);
        bta = findViewById(R.id.bt_a);
        btb = findViewById(R.id.bt_b);
        btc = findViewById(R.id.bt_c);
        btd = findViewById(R.id.bt_d);
        bte = findViewById(R.id.bt_e);
        btf = findViewById(R.id.bt_f);
        btPoint = findViewById(R.id.bt_point);
        btDel = findViewById(R.id.bt_del);

        rdb2.setOnCheckedChangeListener(this);
        rdb8.setOnCheckedChangeListener(this);
        rdb10.setOnCheckedChangeListener(this);
        rdb16.setOnCheckedChangeListener(this);

        et2.setOnClickListener(this);
        et8.setOnClickListener(this);
        et10.setOnClickListener(this);
        et16.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);
        bt0.setOnClickListener(this);
        bta.setOnClickListener(this);
        btb.setOnClickListener(this);
        btc.setOnClickListener(this);
        btd.setOnClickListener(this);
        bte.setOnClickListener(this);
        btf.setOnClickListener(this);
        btPoint.setOnClickListener(this);
        btDel.setOnClickListener(this);

        //添加文本框变化监听
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                Log.d("", "" + select);
                initRadix(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //长按删除全部
        btDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                etText.setText("");
                return false;
            }
        });

        //默认十进制状态下所有按键的激活状态
        bt0.setEnabled(true);
        bt0.setTextColor(colorAccentInt);
        bt1.setEnabled(true);
        bt1.setTextColor(colorAccentInt);
        bt2.setEnabled(true);
        bt2.setTextColor(colorAccentInt);
        bt3.setEnabled(true);
        bt3.setTextColor(colorAccentInt);
        bt4.setEnabled(true);
        bt4.setTextColor(colorAccentInt);
        bt5.setEnabled(true);
        bt5.setTextColor(colorAccentInt);
        bt6.setEnabled(true);
        bt6.setTextColor(colorAccentInt);
        bt7.setEnabled(true);
        bt7.setTextColor(colorAccentInt);
        bt8.setEnabled(true);
        bt8.setTextColor(colorAccentInt);
        bt9.setEnabled(true);
        bt9.setTextColor(colorAccentInt);
        bta.setEnabled(false);
        bta.setTextColor(Color.GRAY);
        btb.setEnabled(false);
        btb.setTextColor(Color.GRAY);
        btc.setEnabled(false);
        btc.setTextColor(Color.GRAY);
        btd.setEnabled(false);
        btd.setTextColor(Color.GRAY);
        bte.setEnabled(false);
        bte.setTextColor(Color.GRAY);
        btf.setEnabled(false);
        btf.setTextColor(Color.GRAY);

        //SharedPreference用来储存设置
        SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
        editor = settings.edit();

        //OnCreate 方法执行时，将shared preference里面的设置赋值给digit
        digit = settings.getInt("Digit", 5);
    }

    //判断选择的进制
    private void initRadix(String input) {

        //若空则返回
        if (TextUtils.isEmpty(input)) {
            et2.setText("");
            et8.setText("");
            et10.setText("");
            et16.setText("");
            return;
        }

        //若最后一位为"."，则返回
        if (etText.getText().toString().substring(etText.getText().toString().length() - 1).equals("."))
            return;

        //判断选择的RadioButton
        try {
            if (select == 2) {
                et2.setText(sendString(input, 2, 2));
                et8.setText(sendString(input, 8, 2));
                et10.setText(sendString(input, 10, 2));
                et16.setText(sendString(input, 16, 2));
            } else if (select == 8) {
                et2.setText(sendString(input, 2, 8));
                et8.setText(sendString(input, 8, 8));
                et10.setText(sendString(input, 10, 8));
                et16.setText(sendString(input, 16, 8));
            } else if (select == 10) {
                et2.setText(sendString(input, 2, 10));
                et8.setText(sendString(input, 8, 10));
                et10.setText(sendString(input, 10, 10));
                et16.setText(sendString(input, 16, 10));
            } else if (select == 16) {
                et2.setText(sendString(input, 2, 16));
                et8.setText(sendString(input, 8, 16));
                et10.setText(sendString(input, 10, 16));
                et16.setText(sendString(input, 16, 16));
            }
        } catch (Exception e) {
            Snackbar.make(etText, "数据格式有误或数值过大", Snackbar.LENGTH_SHORT).show();
            System.out.print(e.toString());
        }
    }

    /**
     * 感谢rtugeek的开源项目的工具类
     */

    //最重要的一部分
    //向进制转换工具类传值，分别是：需要转换的字符串，需要转换到的进制，当前进制，该方法会返回一个结果值
    private String sendString(String input, int toRadix, int fromRadix) {
        String result = "";
        String[] array = input.split("\\.");
        fromRadix = select;
        //if input data only contains integer
        if (!input.contains(".") || array.length == 1) {
            result = RadixUtil.integerConverter(array[0], toRadix, fromRadix);
        } else {
            result = RadixUtil.integerConverter(array[0], toRadix, fromRadix)
                    + "." + RadixUtil.decimalsConverter(array[1], toRadix, fromRadix, digit - 1);
        }
        return result;
    }

    //进制按钮设置监听
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rdb_2:
                if (rdb2.isChecked()) {
                    select = 2;
                    String number = etText.getText().toString();
                    initRadix(number);

                    bt0.setEnabled(true);
                    bt0.setTextColor(colorAccentInt);
                    bt1.setEnabled(true);
                    bt0.setTextColor(colorAccentInt);
                    bt2.setEnabled(false);
                    bt2.setTextColor(Color.GRAY);
                    bt3.setEnabled(false);
                    bt3.setTextColor(Color.GRAY);
                    bt4.setEnabled(false);
                    bt4.setTextColor(Color.GRAY);
                    bt5.setEnabled(false);
                    bt5.setTextColor(Color.GRAY);
                    bt6.setEnabled(false);
                    bt6.setTextColor(Color.GRAY);
                    bt7.setEnabled(false);
                    bt7.setTextColor(Color.GRAY);
                    bt8.setEnabled(false);
                    bt8.setTextColor(Color.GRAY);
                    bt9.setEnabled(false);
                    bt9.setTextColor(Color.GRAY);
                    bta.setEnabled(false);
                    bta.setTextColor(Color.GRAY);
                    btb.setEnabled(false);
                    btb.setTextColor(Color.GRAY);
                    btc.setEnabled(false);
                    btc.setTextColor(Color.GRAY);
                    btd.setEnabled(false);
                    btd.setTextColor(Color.GRAY);
                    bte.setEnabled(false);
                    bte.setTextColor(Color.GRAY);
                    btf.setEnabled(false);
                    btf.setTextColor(Color.GRAY);
                    Log.d("", "" + select);
                }
                break;

            case R.id.rdb_8:
                if (rdb8.isChecked()) {
                    select = 8;
                    String number = etText.getText().toString();
                    initRadix(number);

                    bt0.setEnabled(true);
                    bt0.setTextColor(colorAccentInt);
                    bt1.setEnabled(true);
                    bt1.setTextColor(colorAccentInt);
                    bt2.setEnabled(true);
                    bt2.setTextColor(colorAccentInt);
                    bt3.setEnabled(true);
                    bt3.setTextColor(colorAccentInt);
                    bt4.setEnabled(true);
                    bt4.setTextColor(colorAccentInt);
                    bt5.setEnabled(true);
                    bt5.setTextColor(colorAccentInt);
                    bt6.setEnabled(true);
                    bt6.setTextColor(colorAccentInt);
                    bt7.setEnabled(true);
                    bt7.setTextColor(colorAccentInt);
                    bt8.setEnabled(false);
                    bt8.setTextColor(Color.GRAY);
                    bt9.setEnabled(false);
                    bt9.setTextColor(Color.GRAY);
                    bta.setEnabled(false);
                    bta.setTextColor(Color.GRAY);
                    btb.setEnabled(false);
                    btb.setTextColor(Color.GRAY);
                    btc.setEnabled(false);
                    btc.setTextColor(Color.GRAY);
                    btd.setEnabled(false);
                    btd.setTextColor(Color.GRAY);
                    bte.setEnabled(false);
                    bte.setTextColor(Color.GRAY);
                    btf.setEnabled(false);
                    btf.setTextColor(Color.GRAY);
                    Log.d("", "" + select);
                }
                break;

            case R.id.rdb_10:
                if (rdb10.isChecked()) {
                    select = 10;
                    String number = etText.getText().toString();
                    initRadix(number);

                    bt0.setEnabled(true);
                    bt0.setTextColor(colorAccentInt);
                    bt1.setEnabled(true);
                    bt1.setTextColor(colorAccentInt);
                    bt2.setEnabled(true);
                    bt2.setTextColor(colorAccentInt);
                    bt3.setEnabled(true);
                    bt3.setTextColor(colorAccentInt);
                    bt4.setEnabled(true);
                    bt4.setTextColor(colorAccentInt);
                    bt5.setEnabled(true);
                    bt5.setTextColor(colorAccentInt);
                    bt6.setEnabled(true);
                    bt6.setTextColor(colorAccentInt);
                    bt7.setEnabled(true);
                    bt7.setTextColor(colorAccentInt);
                    bt8.setEnabled(true);
                    bt8.setTextColor(colorAccentInt);
                    bt9.setEnabled(true);
                    bt9.setTextColor(colorAccentInt);
                    bta.setEnabled(false);
                    bta.setTextColor(Color.GRAY);
                    btb.setEnabled(false);
                    btb.setTextColor(Color.GRAY);
                    btc.setEnabled(false);
                    btc.setTextColor(Color.GRAY);
                    btd.setEnabled(false);
                    btd.setTextColor(Color.GRAY);
                    bte.setEnabled(false);
                    bte.setTextColor(Color.GRAY);
                    btf.setEnabled(false);
                    btf.setTextColor(Color.GRAY);
                    Log.d("", "" + select);
                }
                break;

            case R.id.rdb_16:
                if (rdb16.isChecked()) {
                    select = 16;
                    String number = etText.getText().toString();
                    initRadix(number);

                    bt0.setEnabled(true);
                    bt0.setTextColor(colorAccentInt);
                    bt1.setEnabled(true);
                    bt1.setTextColor(colorAccentInt);
                    bt2.setEnabled(true);
                    bt2.setTextColor(colorAccentInt);
                    bt3.setEnabled(true);
                    bt3.setTextColor(colorAccentInt);
                    bt4.setEnabled(true);
                    bt4.setTextColor(colorAccentInt);
                    bt5.setEnabled(true);
                    bt5.setTextColor(colorAccentInt);
                    bt6.setEnabled(true);
                    bt6.setTextColor(colorAccentInt);
                    bt7.setEnabled(true);
                    bt7.setTextColor(colorAccentInt);
                    bt8.setEnabled(true);
                    bt8.setTextColor(colorAccentInt);
                    bt9.setEnabled(true);
                    bt9.setTextColor(colorAccentInt);
                    bta.setEnabled(true);
                    bta.setTextColor(colorAccentInt);
                    btb.setEnabled(true);
                    btb.setTextColor(colorAccentInt);
                    btc.setEnabled(true);
                    btc.setTextColor(colorAccentInt);
                    btd.setEnabled(true);
                    btd.setTextColor(colorAccentInt);
                    bte.setEnabled(true);
                    bte.setTextColor(colorAccentInt);
                    btf.setEnabled(true);
                    btf.setTextColor(colorAccentInt);
                    Log.d("", "" + select);
                }
                break;
        }
    }

    //文本框和按钮设置监听
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.et_2:
                ClipboardManager cm2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm2.setText(et2.getText());
                Snackbar.make(etText, "二进制值：已复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.et_8:
                ClipboardManager cm8 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm8.setText(et8.getText());
                Snackbar.make(etText, "八进制值：已复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.et_10:
                ClipboardManager cm10 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm10.setText(et10.getText());
                Snackbar.make(etText, "十进制值：已复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.et_16:
                ClipboardManager cm16 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm16.setText(et16.getText());
                Snackbar.make(etText, "十六进制值：已复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.bt_point:
                etText.setText(etText.getText() + ".");
                break;

            case R.id.bt_del:
                if (!TextUtils.isEmpty(etText.getText().toString())) {
                    String str = etText.getText().toString();
                    etText.setText(str.substring(0, str.length() - 1));
                }
                break;

            case R.id.bt_0:
                etText.setText(etText.getText() + "0");
                break;

            case R.id.bt_1:
                etText.setText(etText.getText() + "1");
                break;

            case R.id.bt_2:
                etText.setText(etText.getText() + "2");
                break;

            case R.id.bt_3:
                etText.setText(etText.getText() + "3");
                break;

            case R.id.bt_4:
                etText.setText(etText.getText() + "4");
                break;

            case R.id.bt_5:
                etText.setText(etText.getText() + "5");
                break;

            case R.id.bt_6:
                etText.setText(etText.getText() + "6");
                break;

            case R.id.bt_7:
                etText.setText(etText.getText() + "7");
                break;

            case R.id.bt_8:
                etText.setText(etText.getText() + "8");
                break;

            case R.id.bt_9:
                etText.setText(etText.getText() + "9");
                break;

            case R.id.bt_a:
                etText.setText(etText.getText() + "A");
                break;

            case R.id.bt_b:
                etText.setText(etText.getText() + "B");
                break;

            case R.id.bt_c:
                etText.setText(etText.getText() + "C");
                break;

            case R.id.bt_d:
                etText.setText(etText.getText() + "D");
                break;

            case R.id.bt_e:
                etText.setText(etText.getText() + "E");
                break;

            case R.id.bt_f:
                etText.setText(etText.getText() + "F");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //右上角菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_1:
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setHint("精确到小数点后第?位,最大32位");


                new AlertDialog.Builder(this).setTitle("设置精度"+"---当前精度："+digit)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (input.equals("")) {
                                    Toast.makeText(getApplicationContext(), "不能为空！" + input, Toast.LENGTH_LONG).show();
                                } else if (Integer.parseInt(input) > 32) {
                                    Toast.makeText(getApplicationContext(), "不能大于32", Toast.LENGTH_LONG).show();
                                } else {
                                    editor.putInt("Digit", Integer.parseInt(input));
                                    editor.commit();
                                    digit = Integer.parseInt(input);
                                    Log.i("已设置精度到", input);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;

            case R.id.menu_2:
                new AlertDialog.Builder(this).setTitle("关于")
                        .setMessage("一个简单的支持四种进制转换的工具\n" + "算法练手项目\n" + "仅供学习\n\n" + "Author:Yili\n" + "编译时间:" + BUILDTIME)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("博客", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("https://blog.treeoncloud.com");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("主页", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("https://mapotofu.cn");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .show();
                break;
        }
        return true;
    }
}
