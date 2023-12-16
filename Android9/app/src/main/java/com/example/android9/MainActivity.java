package com.example.android9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private TextView tv1, tv2, tv3;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        btn = findViewById(R.id.btnStart);

        btn.setOnClickListener(v -> {
            // Создаем пул из трех потоков
            ExecutorService executor = Executors.newFixedThreadPool(3);

            // Создаем три задачи для вычисления факториалов
            FactorialTask task1 = new FactorialTask(5);
            FactorialTask task2 = new FactorialTask(10);
            FactorialTask task3 = new FactorialTask(15);

            // Отправляем задачи на выполнение в пул потоков
            Future<BigInteger> future1 = executor.submit(task1);
            Future<BigInteger> future2 = executor.submit(task2);
            Future<BigInteger> future3 = executor.submit(task3);

            try {
                // Получаем результаты из Future объектов
                BigInteger result1 = future1.get();
                BigInteger result2 = future2.get();
                BigInteger result3 = future3.get();

                // Обновляем текстовые поля с результатами
                tv1.setText("Факториал 5 = " + result1);
                tv2.setText("Факториал 10 = " + result2);
                tv3.setText("Факториал 15 = " + result3);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Завершаем пул потоков
            executor.shutdown();
        });
    }

    // Класс для вычисления факториала числа
    static class FactorialTask implements Callable<BigInteger> {

        private int number;

        public FactorialTask(int number) {
            this.number = number;
        }

        @Override
        public BigInteger call() throws Exception {
            BigInteger factorial = BigInteger.ONE;
            for (int i = 2; i <= number; i++) {
                factorial = factorial.multiply(BigInteger.valueOf(i));
            }
            return factorial;
        }
    }
}