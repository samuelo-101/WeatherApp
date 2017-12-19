package com.weather.renderer;

import com.weather.adapter.TableAdapter;

/**
 * Renders full table based on data from adapter
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
