package me.zombie_striker.verticality.world;

import me.zombie_striker.verticality.world.blockdata.IBlockData;
import net.minestom.server.coordinate.Pos;

public class BlockStruct {

    private Pos blockLocation;
    private IBlockData[] dataTypes;

    protected BlockStruct(Pos position) {
        this.blockLocation = position;
    }

    public void addBlockData(IBlockData... data) {
        if (dataTypes == null) {
            this.dataTypes = data;
            return;
        }
        IBlockData[] newdata = new IBlockData[dataTypes.length + data.length];
        int index = 0;
        for (IBlockData dataType : dataTypes) {
            newdata[index] = dataType;
            index++;
        }
        for (IBlockData datum : data) {
            newdata[index] = datum;
            index++;
        }
        this.dataTypes = newdata;
    }

    public IBlockData[] getBlockData() {
        return dataTypes;
    }

    public Pos getBlockLocation() {
        return blockLocation;
    }

    public void setBlockLocation(Pos blockLocation) {
        this.blockLocation = blockLocation;
    }
}
