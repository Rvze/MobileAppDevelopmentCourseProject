import requests

SUBSCRIBE_ENDPOINT = "http://51.250.101.189:8080/v1/notification/stuff"
NOTIFICATION_ENDPOINT = "http://51.250.101.189:8080/v1/notification/list"

SUPPLIER_REGISTER_ENDPOINT = "http://51.250.101.189:8080/register"
SUPPLIER_LOGIN_ENDPOINT = "http://51.250.101.189:8080/login"

ADD_STUFF_ENDPOINT = "http://51.250.101.189:8080/v1/supplier/stuff"
user_id = int()
access_token = str()


def test_register():
    username = "python"
    password = "python"
    role = "SUPPLIER"
    body = {
        "username": username,
        "password": password,
        "role": role
    }
    global access_token
    global user_id
    response = requests.post(SUPPLIER_REGISTER_ENDPOINT, json=body)
    if response.status_code == 200:
        access_token = response.headers['X-Access-Token']
        user_id = response.headers['Userid']
    else:
        response = requests.post(SUPPLIER_LOGIN_ENDPOINT, json=body)
        access_token = response.headers['X-Access-Token']
        user_id = response.headers['Userid']


def test_add_iphone():
    headers = {
        'UserID': str(user_id),
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Access-Token': access_token
    }
    body = {
        "text": "14 PRO 512 black 150000.00 🇺🇸"
    }
    response = requests.post(ADD_STUFF_ENDPOINT, headers=headers, json=body)
    assert response.status_code == 200


def test_register_buyer():
    username = "buyer"
    password = "buyer"
    role = "STUFF"
    body = {
        "username": username,
        "password": password,
        "role": role
    }
    global access_token
    global user_id
    response = requests.post(SUPPLIER_REGISTER_ENDPOINT, json=body)
    if response.status_code == 200:
        access_token = response.headers['X-Access-Token']
        user_id = response.headers['Userid']
    else:
        response = requests.post(SUPPLIER_LOGIN_ENDPOINT, json=body)
        access_token = response.headers['X-Access-Token']
        user_id = response.headers['Userid']
    assert response.status_code == 200


def test_subscribe_on_model():
    headers = {
        'UserID': str(user_id),
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Access-Token': access_token
    }
    body = {
        "action_type": "SUBSCRIBE",
        "model_id": "IPHONE_14_PRO/GB_512/SPACE_BLACK"
    }
    response = requests.post(SUBSCRIBE_ENDPOINT, json=body, headers=headers)
    assert response.status_code == 200


def test_change_price():
    headers = {
        'UserID': str(user_id),
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Access-Token': access_token
    }
    body = {
        "text": "14 PRO 512 black 160000.00 🇺🇸"
    }
    response = requests.post(ADD_STUFF_ENDPOINT, headers=headers, json=body)
    assert response.status_code == 200


def test_get_list_notification():
    headers = {
        'UserID': str(user_id),
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Access-Token': access_token
    }
    response = requests.get(NOTIFICATION_ENDPOINT, headers=headers)
    assert response.status_code == 200
    assert response.text != ""
