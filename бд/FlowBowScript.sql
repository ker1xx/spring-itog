-- Создание базы данных
CREATE DATABASE FlowBow;
GO

USE FlowBow;
GO

-- Таблицы
CREATE TABLE dbo.AuthorizationData
(
    AuthorizationDataId INT IDENTITY PRIMARY KEY NOT NULL,
    Login NVARCHAR(255) NOT NULL,
    Password NVARCHAR(MAX) NOT NULL,
    Salt NVARCHAR(255) NOT NULL
);

CREATE TABLE dbo.Administrators
(
    AdministratorId INT IDENTITY PRIMARY KEY NOT NULL,
    Name NVARCHAR(255) NOT NULL,
    Lastname NVARCHAR(255) NOT NULL,
    Patronymic NVARCHAR(255),
    AuthorizationDataId INT,
    FOREIGN KEY (AuthorizationDataId) REFERENCES dbo.AuthorizationData(AuthorizationDataId)
);

CREATE TABLE dbo.PersonalData
(
    PersonalDataId INT IDENTITY PRIMARY KEY NOT NULL,
    Name NVARCHAR(255) NOT NULL,
    Lastname NVARCHAR(255) NOT NULL,
    Patronymic NVARCHAR(255),
    PhoneNumber NVARCHAR(50) NOT NULL,
    Salt NVARCHAR(255) NOT NULL
);

CREATE TABLE dbo.Clients
(
    ClientId INT IDENTITY PRIMARY KEY NOT NULL,
    AuthorizationDataId INT NOT NULL,
    PersonalDataId INT NOT NULL,
    FOREIGN KEY (AuthorizationDataId) REFERENCES dbo.AuthorizationData(AuthorizationDataId),
    FOREIGN KEY (PersonalDataId) REFERENCES dbo.PersonalData(PersonalDataId)
);

CREATE TABLE dbo.ShippingAddresses
(
    ShippingAddressId INT IDENTITY PRIMARY KEY NOT NULL,
    ClientId INT NOT NULL,
    StreetName NVARCHAR(255) NOT NULL,       
    HouseNumber NVARCHAR(50) NOT NULL,        
    Building NVARCHAR(50),                      
    ApartmentNumber NVARCHAR(50),              
    City NVARCHAR(255) NOT NULL,                
    Region NVARCHAR(255),                        
    Postal_code NVARCHAR(20) NOT NULL,        
    Country NVARCHAR(255) NOT NULL,             
    Salt NVARCHAR(255) NOT NULL,
    FOREIGN KEY (ClientId) REFERENCES dbo.Clients(ClientId)
);

CREATE TABLE dbo.OrderStatuses
(
    OrderStatusId INT IDENTITY PRIMARY KEY NOT NULL,
    OrderStatusName NVARCHAR(255) NOT NULL
);

CREATE TABLE dbo.Orders
(
    OrderId INT IDENTITY PRIMARY KEY NOT NULL,
    Date DATE NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Comment NVARCHAR(MAX),
    ClientId INT NOT NULL,
    AddressId INT NOT NULL,
    StatusId INT NOT NULL,
    FOREIGN KEY (ClientId) REFERENCES dbo.Clients(ClientId),
    FOREIGN KEY (AddressId) REFERENCES dbo.ShippingAddresses(ShippingAddressId),
    FOREIGN KEY (StatusId) REFERENCES dbo.OrderStatuses(OrderStatusId)
);

CREATE TABLE dbo.Tags
(
    TagId INT IDENTITY PRIMARY KEY NOT NULL,
    TagName NVARCHAR(255) NOT NULL
);

CREATE TABLE dbo.Flowers
(
    FlowerId INT IDENTITY PRIMARY KEY NOT NULL,
    FlowerName NVARCHAR(255) NOT NULL,
    Amount INT NOT NULL,
    IsAvailable BIT NOT NULL 
);

CREATE TABLE dbo.FlowerTag
(
    FlowerId INT NOT NULL,
    TagId INT NOT NULL,
    PRIMARY KEY (FlowerId, TagId),
    FOREIGN KEY (FlowerId) REFERENCES dbo.Flowers(FlowerId),
    FOREIGN KEY (TagId) REFERENCES dbo.Tags(TagId)
);

CREATE TABLE dbo.Bouquets
(
    BouquetId INT IDENTITY PRIMARY KEY NOT NULL,
    BouquetName NVARCHAR(255) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Amount INT NOT NULL,
    IsAvailable BIT
);

CREATE TABLE dbo.BouquetTag
(
    BouquetId INT NOT NULL,
    TagId INT NOT NULL,
    PRIMARY KEY (BouquetId, TagId),
    FOREIGN KEY (BouquetId) REFERENCES dbo.Bouquets(BouquetId),
    FOREIGN KEY (TagId) REFERENCES dbo.Tags(TagId)
);

CREATE TABLE dbo.BouquetFlower
(
    BouquetId INT NOT NULL,
    FlowerId INT NOT NULL,
    PRIMARY KEY (BouquetId, FlowerId),
    FOREIGN KEY (BouquetId) REFERENCES dbo.Bouquets(BouquetId),
    FOREIGN KEY (FlowerId) REFERENCES dbo.Flowers(FlowerId)
);

CREATE TABLE dbo.OrderItems
(
    OrderItemId INT IDENTITY PRIMARY KEY NOT NULL,
    Quantity INT NOT NULL,
    FlowerId INT,
    BouquetId INT,
    OrderId INT NOT NULL,
    FOREIGN KEY (FlowerId) REFERENCES dbo.Flowers(FlowerId),
    FOREIGN KEY (BouquetId) REFERENCES dbo.Bouquets(BouquetId),
    FOREIGN KEY (OrderId) REFERENCES dbo.Orders(OrderId)
);

-- Представления
CREATE VIEW dbo.FlowerDetails AS
SELECT 
    f.FlowerName,
    f.Amount,
    f.IsAvailable,
    STRING_AGG(t.TagName, ', ') AS Tags
FROM dbo.Flowers f
LEFT JOIN dbo.FlowerTag ft ON f.FlowerId = ft.FlowerId
LEFT JOIN dbo.Tags t ON ft.TagId = t.TagId
GROUP BY f.FlowerId, f.FlowerName, f.Amount, f.IsAvailable;

CREATE VIEW dbo.BouquetDetails AS
SELECT 
    b.BouquetName,
    b.Amount,
    b.IsAvailable,
    STRING_AGG(t.TagName, ', ') AS Tags
FROM dbo.Bouquets b
LEFT JOIN dbo.BouquetTag bt ON b.BouquetId = bt.BouquetId
LEFT JOIN dbo.Tags t ON bt.TagId = t.TagId
GROUP BY b.BouquetId, b.BouquetName, b.Amount, b.IsAvailable;

CREATE VIEW dbo.AvailableBouquets AS
SELECT 
    b.BouquetId,
    b.BouquetName,
    b.Price,
    b.Amount,
    b.IsAvailable
FROM dbo.Bouquets b
JOIN dbo.BouquetFlower bf ON b.BouquetId = bf.BouquetId
JOIN dbo.Flowers f ON bf.FlowerId = f.FlowerId
GROUP BY b.BouquetId, b.BouquetName, b.Price, b.Amount, b.IsAvailable
HAVING SUM(CASE WHEN f.IsAvailable = 0 THEN 1 ELSE 0 END) = 0;
