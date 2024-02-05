package nl.hva.hvacrawler.business.domain;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 07/09/2023 16:28
 */
public class Nothing extends Item{
    public Nothing() {
        super(44, "nothing");
    }

    public Nothing(int id, String itemName) {
        super(id, itemName);
    }

    @Override
    public String toString() {
        return "Nothing{name: "+ this.getName() + ", id: "+ this.getId() +"}";
    }
}
