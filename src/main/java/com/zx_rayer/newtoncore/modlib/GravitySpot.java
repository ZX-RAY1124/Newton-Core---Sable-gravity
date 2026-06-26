package com.zx_rayer.newtoncore.modlib;

public class GravitySpot {
    public double phi = 0;
    public double rho = 0;
    public final double x;
    public final double y;
    public final double z;
    private GravitySpot(double x, double y, double z, double phi, double rho){
        this.phi = phi;
        this.rho = rho;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double GetPhi() {
        return this.phi;
    }





}
