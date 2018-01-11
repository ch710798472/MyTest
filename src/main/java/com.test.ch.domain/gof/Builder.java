package com.test.ch.domain.gof;

/**
 * builder设计模式，对象初始化简洁明了
 */
public class Builder {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String phone;
    private final String address;

    /**
     * 私有方法
     * @param builder
     */
    private Builder(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public static class UserBuilder {
        private final String firstName;
        private final String lastName;
        private int age;
        private String phone;
        private String address;

        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public Builder build() {
            return new Builder(this);
        }
    }

    public static void main(String[] args) {
        new Builder.UserBuilder("张","三").age(2).address("213").build();
    }
}
