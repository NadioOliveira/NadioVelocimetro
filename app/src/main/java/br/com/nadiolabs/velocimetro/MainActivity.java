package br.com.nadiolabs.velocimetro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    SwitchCompat swMtrica;
    TextView tvVelocidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swMtrica = findViewById(R.id.swMetrica);
        tvVelocidade = findViewById(R.id.tvVelocidade);

        // verifica a permissão do gps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            // liga o programa se a verificação for feita
            startPorgram();
        }

        this.atualizaVelocidade(null);

        swMtrica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this.atualizaVelocidade(null);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation minhaLocalizacao = new CLocation(location, this.useUnidadeMetrica());
            this.atualizaVelocidade(minhaLocalizacao);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void startPorgram() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "Esperando por conexão do GPS!", Toast.LENGTH_SHORT).show();
    }

    private void atualizaVelocidade(CLocation location) {
        float velocidadeAtual = 0;

        if (location != null) {
            location.setUnidadeMetrica(this.useUnidadeMetrica());
            velocidadeAtual = location.getSpeed();
        }

        Formatter formatter = new Formatter(new StringBuilder());
        formatter.format(Locale.US, "%5.1f", velocidadeAtual);
        String strVelocidadeAtual = formatter.toString();
        strVelocidadeAtual = strVelocidadeAtual.replace(" ","0");

        String strUnidade;
        if(this.useUnidadeMetrica()) {
            tvVelocidade.setText(strVelocidadeAtual);
            //tvVelocidade.setText(strVelocidadeAtual +"Km/h");
        } else {
            tvVelocidade.setText(strVelocidadeAtual);
            //tvVelocidade.setText(strVelocidadeAtual +"milesimo/hora");

        }
    }

    public boolean useUnidadeMetrica(){
        return swMtrica.isChecked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startPorgram();
            } else {
                finish();
            }
        }
    }
}
