/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.OrderDAO;
import DTO.OrderDTO;
import java.util.ArrayList;
import java.util.List;

public class OrderBUS {
    
    private List <OrderDTO> orderlist;
    
    public OrderBUS() {
        this.orderlist = new ArrayList<>();
    }
    
    public List<OrderDTO> getAllOrder() {
        return new ArrayList<>(orderlist);
    }
    
    public void updateOrder(OrderDTO order) {
        for(int i = 0; i < orderlist.size(); i++) {
            if(orderlist.get(i).getorderID().equals(order.getorderID())) {
                orderlist.set(i, order);
                return;
            }
        }
    }
    
    public void setOrder(List<OrderDTO> order) {
        this.orderlist = new ArrayList<>(order);
    }
}
