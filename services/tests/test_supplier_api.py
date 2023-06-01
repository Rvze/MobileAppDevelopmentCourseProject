import json
from types import SimpleNamespace

import requests

POST_ENDPOINT = "http://51.250.101.189:8080/v1/supplier/stuff"
GET_ENDPOINT = "http://51.250.101.189:8080/v1/supplier/stuff"
headers = {'UserID': '7',
           'Content-Type': 'application/json',
           'Accept': 'application/json',
           'X-Access-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODU4MzUxNTMsInJvbGUiOiJTVVBQTElFUiIsInVzZXJuYW1lIjoibnVyZ3VuIn0.TCMWWosp5Nf5_sYYhczXPsT24pLDsCcpInntxUOJL98'}
body = {"text": "14 PRO MAX 512 black 150000.00 🇺🇸"}


def test_add_iphone():
    response = requests.post(POST_ENDPOINT, headers=headers, json=body)
    assert response.status_code == 200


def test_get_stuff():
    response = requests.get(GET_ENDPOINT, headers=headers)
    iphones = json.loads(response.text, object_hook=lambda d: SimpleNamespace(**d))
    print(iphones)
    print("\n")
    assert response.status_code == 200
    for iphone in iphones:
        for price in iphone.supplierPrices:
            print(price)
            if price.supplierId == 7:
                assert price.price.amount == 150000.0
