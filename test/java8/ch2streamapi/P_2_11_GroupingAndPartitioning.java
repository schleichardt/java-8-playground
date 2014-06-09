package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static base.Demo.Person;

public class P_2_11_GroupingAndPartitioning {

    static class Customer extends Person {
        private final String customerGroup;

        public Customer(String name, int id, String customerGroup) {
            super(name, id);
            this.customerGroup = customerGroup;
        }

        public String getCustomerGroup() {
            return customerGroup;
        }

        public int getAge() {
            return 25;
        }
    }

    /*
    grouping by groupes by values, so it can be more than 2  entries
     */
    @Test
    public void groupingBy() throws Exception {
        final Map<String, List<Customer>> customerGroupToCustomer = newCustomerStream()
                .collect(Collectors.groupingBy(Customer::getCustomerGroup));
    }

    /*
    if you want to use predicate => Map will at max contain two entries
     */
    @Test
    public void partitioningBy() throws Exception {
        final Map<Boolean, List<Customer>> customerGroupToCustomer = newCustomerStream()
                        .collect(Collectors.partitioningBy(customer -> customer.getCustomerGroup().equals("B2C")));

    }

    private Stream<Customer> newCustomerStream() {
        return Stream.of(new Customer("1", 1, "B2B"), new Customer("2", 2, "B2C"));
    }


    @Test
    public void counting() throws Exception {
        final Map<String, Long> groupToCustomerInGroup =
                newCustomerStream().collect(Collectors.groupingBy(Customer::getCustomerGroup, Collectors.counting()));

        /*
        similar: summingInt, maxBy comparator, minBy Comparator
         */
    }

    @Test
    public void statistics() throws Exception {
        final Map<String, IntSummaryStatistics> statisticsMap = newCustomerStream().collect(Collectors.
                groupingBy(Customer::getCustomerGroup, Collectors.summarizingInt(cu -> cu.getAge())));

        final IntSummaryStatistics b2bStatistic = statisticsMap.get("B2B");
        final double average = b2bStatistic.getAverage();
        final long count = b2bStatistic.getCount();
        final int max = b2bStatistic.getMax();
        final int min = b2bStatistic.getMin();
        final long sum = b2bStatistic.getSum();
    }

    @Test
    public void reducing() throws Exception {
        //TODO
    }
}
