package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // создание полей для вывода на экран нужных значений
    private Button buttonStart; // кнопка запуска секундомера
    private Button buttonPause; // кнопка паузы секундомера
    private Button buttonRevers; // кнопка обратного отчета секундомера
    private TextView stopwatchOut; // поле вывода результирующей информации

    // дополнительное поля логики
    private long startTime = 0L; // стартовое время
    private long timeInMilliseconds = 0L; // текущее время в миллисекундах
    private long timePause = 0L; // время в состоянии "Пауза"
    private long updatedTime = 0L; // обновлённое время
    private boolean timeMode; // режим отчета времени (True - время вперед, False - время назад)


    private Handler handler = new Handler(); // обработчик очереди сообщений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // присваивание переменным активити элементов представления activity_main
        buttonStart = findViewById(R.id.buttonStart); // кнопка обработки
        buttonPause = findViewById(R.id.buttonPause); // кнопка обработки
        buttonRevers = findViewById(R.id.buttonRevers); // кнопка обработки
        stopwatchOut = findViewById(R.id.stopwatchOut); // поле вывода

        // выполнение действий при нажании кнопки
        buttonStart.setOnClickListener(listener); // обработка нажатия кнопки
        buttonPause.setOnClickListener(listener); // обработка нажатия кнопки
        buttonRevers.setOnClickListener(listener); // обработка нажатия кнопки

    }
    // объект обработки нажатия всех кнопок
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { // при нажатии кнопки во view записывается значение кнопки

            // через view.getId() можно прочитать значение ID кнопки

            switch (view.getId()) { // switch (значение выбранного ID кнопки)
                case R.id.buttonStart: // нажатие кнопки "Старт"
                    timeMode = true;
                    startTime = SystemClock.uptimeMillis(); // Миллисекунды с момента загрузки (не считая времени, проведенного в глубоком сне)
                    handler.postDelayed(updateTimerThread, 0); // запуск потока с нулевой задержкой
                    break;
                case R.id.buttonPause: // нажатие кнопки "Пауза"
                    timePause = 0L;
                    timePause = updatedTime; // фиксирование времени в момент нажатия кнопки
                    handler.removeCallbacks(updateTimerThread); // удаление из очереди данного потока
                    break;
                case R.id.buttonRevers: // нажатие кнопки "Назад"
                    timeMode = false;
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    break;
            }
        }
    };
    // создание нового потока для обновления времени с помощью объекта интерфейса Runnable
    private Runnable updateTimerThread = new Runnable() {
        public void run() { // внутри метода run() помещается код выполняемого потока
            if(timeMode) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime; // определение текущего времени
                updatedTime = timePause + timeInMilliseconds; // обновлённое время
            } else {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime; // определение текущего времени
                updatedTime = timePause - timeInMilliseconds; // обновлённое время
            }
            int milliseconds = (int) (updatedTime % 1000); // определение количества миллисекунд
            int second = (int) (updatedTime / 1000); // определение количества секунд
            int minute = second / 60; // определение количества минут
            int hour = minute / 60; // определение количества часов
            int day = hour / 24; // определение количества дней

            second = second % 60; // ограничение количества секунд 60 секундами
            minute = minute % 60; // ограничение количества минут 60 минутами
            hour = hour % 24; // ограничение количества часов 24 часами

            // запись времени в окне вывода информации
            stopwatchOut.setText("" + day + ":" + hour + ":" + minute + ":" + String.format("%02d", second) + ":" + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0); // запуск потока с нулевой задержкой
        }
    };
}