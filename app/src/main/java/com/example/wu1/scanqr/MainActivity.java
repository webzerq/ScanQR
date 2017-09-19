package com.example.wu1.scanqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//QR-код
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//JSON
import org.json.JSONException;
import org.json.JSONObject;

//"Прослушиваем" нажатие на кнопку. onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Переменные для хранения кнопки сканирования, поля Name и поля Address
    private Button buttonScan;
    private TextView textViewName, textViewAddress;

    //Класс IntentIntegrator для сканирования QR-кода
    private IntentIntegrator qrScan;
    //@Override указывает на переопределение метода базового класса.
    //Служит для контроля успешности действия при сборке
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Метод findViewById производит поиск компонента по ID из activity_main.xml
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        //new создает новый объект qrScan на основе IntentIntegrator
        qrScan = new IntentIntegrator(this);

        //Обработчик setOnClickListener(this) для кнопки buttonScan
        buttonScan.setOnClickListener(this);
    }

    //Получаем результаты сканирования
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //Если QR-код ничего не сожержит
            if (result.getContents() == null) {
                Toast.makeText(this, "Информации не обнаружено", Toast.LENGTH_LONG).show();
            } else {
                //Если QR-код содержит информацию
                try {
                    //Преобразование информации в JSON
                    JSONObject obj = new JSONObject(result.getContents());
                    //Подстановка значений для name и address
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Если в процессе чтения QR-кода
                    //данные в JSON указаны не в соответствии с заданным форматом
                    //прочитанная информация всплывает в окне приложения
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //Инициирование сканирования QR-кода
        qrScan.initiateScan();
    }
}