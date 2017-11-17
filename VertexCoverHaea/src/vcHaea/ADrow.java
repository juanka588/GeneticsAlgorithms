/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.util.ArrayList;

/**
 *
 * @author JuanCamilo
 */
public class ADrow {

    public ADrow(ArrayList<Integer> row, int rowid) {
        this.row = row;
        this.rowid = rowid;
    }
    ArrayList<Integer> row;
    int rowid;
    int greaterSum;

    public void add(Integer obj) {
        row.add(obj);
    }
    public Integer get(int i){
        return row.get(i);
    }
    public int calGreaters() {
        greaterSum = 0;
        for (int i = 0; i < row.size(); i++) {
            if (rowid < row.get(i)) {
                greaterSum++;
            }
        }
        return greaterSum;
    }

    public int size() {
        return row.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < row.size(); i++) {
            sb.append(row.get(i));
            sb.append(' ');
        }
        sb.append('i');
        sb.append(rowid);
        return sb.toString();
    }

}
