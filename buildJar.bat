cd .\bmsreact
start npm run build
cd ..
cd .\src\main\resources\static
del *
rmdir /S static
cd ..\..\..\..\
cd .\target
del *
cd ..
mvn install -DskipTests