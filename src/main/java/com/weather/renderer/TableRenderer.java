package com.weather.renderer;

import com.weather.adapter.TableAdapter;

/**
 * Created by samuelojo on 2017/12/17.
 */
public class TableRenderer {

    private TableAdapter tableAdapter;

    public void setAdapter(TableAdapter tableAdapter) {
        this.tableAdapter = tableAdapter;
    }

    public TableAdapter getAdapter() {
        return this.tableAdapter;
    }

    public void render() {
        for(int index = 0; index < tableAdapter.getSize(); index++) {
            System.out.println(tableAdapter.renderItem(index));
        }
    }
}
