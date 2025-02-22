# Perbaikan code 

## 1. endpoint get all pasien 
- optimalisasi pada query: 
<br>
karena model database 1 + n maka jika tidak dioptimalisasi maka execute query bakalan banyak maka menggunakan joint fetch contoh kode :
<br>
pada repository : 
<br>
`
@Query("SELECT p FROM Patient p LEFT JOIN FETCH p.prescriptions pr LEFT JOIN FETCH pr.drugs d LEFT JOIN FETCH d.drugDetail")
List<Patient> findAllWithPrescriptionsAndDrugs();
`
