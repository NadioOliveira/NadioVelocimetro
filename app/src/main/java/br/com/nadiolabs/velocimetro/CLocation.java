package br.com.nadiolabs.velocimetro;

import android.location.Location;

/**
 * Created by Nadio Oliveira on 21/07/2019.
 */
public class CLocation extends android.location.Location {

    private boolean unidadeMetrica = false;

    public CLocation(Location location) {
        this(location, true);
    }

    public CLocation(Location location, boolean unidadeMetrica) {
        super(location);
        this.unidadeMetrica = unidadeMetrica;
    }

    public boolean getUnidadeMetrica() {
        return this.unidadeMetrica;
    }

    public void setUnidadeMetrica(boolean unidadeMetrica) {
        this.unidadeMetrica = unidadeMetrica;
    }

    @Override
    public float getSpeed() {
        float velocidade = super.getSpeed();

        if (!this.getUnidadeMetrica()) {
            // converte metros/segundos para milesimo/hora
            velocidade = super.getSpeed() * 2.23693629f;
        }

        return velocidade;
    }

    @Override
    public float distanceTo(Location dest) {
        float distancia = super.distanceTo(dest);

        if (!this.getUnidadeMetrica()) {
            // converte metros para passos
            distancia = distancia * 3.28083989501312f;
        }

        return distancia;
    }

    @Override
    public double getAltitude() {
        double altitude = super.getAltitude();

        if (!this.getUnidadeMetrica()) {
            // converte metros para passos
            altitude = altitude * 3.28083989501312f;
        }

        return altitude;
    }

    @Override
    public float getAccuracy() {
        float precisao = super.getAccuracy();

        if (!this.getUnidadeMetrica()) {
            // converte metros para passos
            precisao = precisao * 3.28083989501312f;
        }

        return precisao;
    }
}
