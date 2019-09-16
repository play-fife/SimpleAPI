package com.tatenda.MoneyTransfer;

public class Account
{
  private String accountNumber;
  private Double balance;

  public Account(String accountNumber, double balance)
  {
    this.accountNumber = accountNumber;
    this.balance = balance;
  }

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public double getBalance()
  {
    return balance;
  }

  public void setBalance(Double balance)
  {
    this.balance = balance;
  }

  public void transfer(Double amount)
  {
    synchronized (this)
      {
      this.balance += amount;
      }

  }
}
