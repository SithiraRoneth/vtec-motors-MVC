DROP DATABASE IF EXISTS vtec;
CREATE DATABASE vtec;
use vtec;

create table User(
                     user_name VARCHAR(100)PRIMARY KEY ,
                     password VARCHAR(100)

);

create table Employee(
                         Emp_id VARCHAR(100)PRIMARY KEY,
                         Emp_name VARCHAR(100),
                         Contact_no VARCHAR(15),
                         NIC VARCHAR(155),
                         Job VARCHAR(100)
);



create table Supplier(
                         Supplier_id VARCHAR(100)PRIMARY KEY ,
                         Supplier_name VARCHAR(100),
                         Supplier_ContactNo VARCHAR(15)
);


create table Guardian(
                         Guardian_id VARCHAR(100)PRIMARY KEY ,
                         Guardian_name VARCHAR(100),
                         Guardian_ContactNo VARCHAR(15),
                         Emp_id VARCHAR(100),
                         FOREIGN KEY (Emp_id)REFERENCES Employee(Emp_id)ON UPDATE CASCADE ON DELETE CASCADE
);

create table Appointments(
                             App_id VARCHAR(100)PRIMARY KEY ,
                             App_date DATE,
                             Guardian_id VARCHAR(100),
                             FOREIGN KEY (Guardian_id)REFERENCES Guardian(Guardian_id)ON UPDATE CASCADE ON DELETE CASCADE
);

create table Vehicles(
                         Vehicle_id VARCHAR(100)PRIMARY KEY ,
                         Vehicle_Type VARCHAR(100),
                         Guardian_id VARCHAR(100),
                         FOREIGN KEY (Guardian_id)REFERENCES Guardian(Guardian_id)ON UPDATE CASCADE ON DELETE CASCADE
);

create table Orders(
                       Order_id VARCHAR(100)PRIMARY KEY ,
                       Order_date DATE,
                       Guardian_id VARCHAR(100),
                       FOREIGN KEY (Guardian_id)REFERENCES Guardian(Guardian_id)ON UPDATE CASCADE ON DELETE CASCADE
);

create table Service(
                        Service_id VARCHAR(100)PRIMARY KEY ,
                        Service_name VARCHAR(200),
                        Service_description VARCHAR(100),
                        Amount VARCHAR(100)
);
create table SpareParts(
                           Spare_id VARCHAR(100)PRIMARY KEY ,
                           Spare_type VARCHAR(100),
                           Description VARCHAR(100),
                           price double,
                           Service_name VARCHAR(200),
                           Service_id VARCHAR(100),
                           FOREIGN KEY(Service_id)REFERENCES service(Service_id)ON UPDATE CASCADE ON DELETE CASCADE

);

create table OrderService(
                             Order_id VARCHAR(100),
                             Service_id VARCHAR(100),
                             price double,
                            FOREIGN KEY (Order_id)REFERENCES Orders(Order_id)ON UPDATE CASCADE ON DELETE CASCADE ,
                            FOREIGN KEY (Service_id)REFERENCES Service(Service_id)ON UPDATE CASCADE ON DELETE CASCADE
);


create table SpareParts_Supplier(
                                    Supplier_id VARCHAR(100),
                                    Supplier_name VARCHAR(100),
                                    Spare_id VARCHAR(100),
                                    Spare_type VARCHAR(100),
                                    FOREIGN KEY (Supplier_id)REFERENCES Supplier(Supplier_id)ON UPDATE CASCADE ON DELETE CASCADE ,
                                    FOREIGN KEY (Spare_id)REFERENCES SpareParts(Spare_id)ON UPDATE CASCADE ON DELETE CASCADE
);

select *  from Employee;


create table income(
                       income_id INT AUTO_INCREMENT PRIMARY KEY ,
                       description VARCHAR(100),
                       Amount double,
                       year INT,
                       month VARCHAR(100),
                       date DATE
);

create table expenditure(
                            expenditure_id INT AUTO_INCREMENT PRIMARY KEY ,
                            description VARCHAR(100),
                            Amount double,
                            year INT,
                            month VARCHAR(100),
                            date DATE
);
create table attendance(
                           attendance_id INT AUTO_INCREMENT PRIMARY KEY ,
                           Date DATE NOT NULL ,
                           Emp_Id VARCHAR(100),
                           Emp_Name VARCHAR(100),
                             FOREIGN KEY (Emp_Id) REFERENCES Employee(Emp_id)on update cascade on DELETE cascade
);

create table Salary(
    salary_id INT AUTO_INCREMENT PRIMARY KEY ,
    Emp_id VARCHAR(100),
    Emp_name VARCHAR(100),
    salary_amount double,
    bonus double,
    etf double,
    final_salary double,
    FOREIGN KEY (Emp_id)REFERENCES Employee(Emp_id)ON UPDATE CASCADE ON DELETE CASCADE
);

drop table user;

create TABLE user(
    username VARCHAR(100)PRIMARY KEY ,
    email VARCHAR(100),
    password VARCHAR(100)
);



