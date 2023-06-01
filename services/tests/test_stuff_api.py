import json
from types import SimpleNamespace

import requests

SEARCH_ENDPOINT = "http://51.250.101.189:8080/v1/stuff/search-by-text"
BUY_ENDPOINT = "http://51.250.101.189:8080/v1/stuff/buy-request"
headers = {'UserID': '7',
           'Content-Type': 'application/json',
           'Accept': 'application/json',
           'X-Access-Token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODU4MzUxNTMsInJvbGUiOiJTVVBQTElFUiIsInVzZXJuYW1lIjoibnVyZ3VuIn0.TCMWWosp5Nf5_sYYhczXPsT24pLDsCcpInntxUOJL98'}
search_text = {"text": "14 PRO MAX 512 black 150000.00 🇺🇸"}
expected_response = {"stuffType": "IPHONE", "modelId": "IPHONE_14_PRO_MAX GB_512 SPACE_BLACK USA",
                     "title": "iPhone 14 Pro Max",
                     "properties": [{"name": "MEMORY", "value": "512 ГБ"}, {"name": "COLOR", "value": "Space Black"}],
                     "supplierPrices": [{"supplierId": 7, "price": {"amount": 150000.0, "currency": "RUB"}}]}
buy_request = {
    "supplier_id": "7",
    "model_id": "IPHONE_14_PRO_MAX/GB_512/SPACE_BLACK",
    "count": "5"
}


class Struct:
    def __init__(self, **entries):
        self.__dict__.update(entries)


def test_search_by_text():
    response = requests.post(SEARCH_ENDPOINT, headers=headers, json=search_text)
    required_response = json.loads(response.text, object_hook=lambda d: SimpleNamespace(**d))
    expected = Struct(**expected_response)
    for response in required_response:
        if response.modelId == expected.modelId:
            assert True
        for i in range(len(response.properties)):
            if response.properties[i].name == expected.properties[i]['name']:
                assert response.properties[i].value == expected.properties[i]['value']
        for i in range(len(response.supplierPrices)):
            req_supplier_prices = response.supplierPrices
            expected_supplier_prices = expected.supplierPrices
            if req_supplier_prices[i].supplierId == expected_supplier_prices[i]['supplierId']:
                assert req_supplier_prices[i].price.amount == expected_supplier_prices[i]['price']['amount']


def test_buy_request():
    response = requests.post(BUY_ENDPOINT, headers=headers, json=buy_request)
    assert response.status_code == 200
