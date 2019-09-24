package ru.javawebinar.basejava;

public class MainDeadlock {

    private static class Account {
        Double account;
        String name;

        public Account(String name, Double account) {
            this.name = name;
            this.account = account;
        }
    }

    private static void transfer(Account fromAccount, Account toAccount, Double amount) {
        synchronized (fromAccount) {
            System.out.println(Thread.currentThread().getName() + " locked fromAccount: " + fromAccount.name);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " is waiting for toAccount: " + toAccount.name);
            synchronized (toAccount) {
                fromAccount.account = fromAccount.account - amount;
                toAccount.account = toAccount.account + amount;
            }
        }
    }

    public static void main(String[] args) {
        Account account1 = new Account("first", 100.);
        Account account2 = new Account("second", 50.);

        new Thread(() -> transfer(account1, account2, 10.)).start();
        new Thread(() -> transfer(account2, account1, 20.)).start();
    }
}
