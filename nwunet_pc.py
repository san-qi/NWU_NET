import json
import psutil
import requests
import time


# UID表示学号, PASSWD表示校园网密码
# 使用时请修改此处 或给main传参
UID = '2018117197'
PASSWD = '123456'


# 获取当前wifi对应的mac地址与ip地址
def get_address():
    dic = psutil.net_if_addrs()
    mac = None
    ipv4 = None
    for adapter in dic:
        if adapter.lower() != 'wlan':
            continue
        snicList = dic[adapter]
        for snic in snicList:
            if snic.family.name in {'AF_LINK', 'AF_PACKET'}:
                mac = snic.address
            elif snic.family.name == 'AF_INET':
                ipv4 = snic.address
        return mac, ipv4
    return mac, ipv4


def check_list(headers):
    """
        检查是否已经有两台设备登陆
        若有则使其中一个下线
        优先下线名为unknown的设备, 其次是PC
    """
    base_url = 'http://10.16.0.12:8081/portal/api/v2/session'
    r = requests.get(base_url+f'/list?noCache={int(time.time()*1000)}', headers=headers)

    check_list = json.loads(r.text)['sessions']
    acct_unique_id = None
    device_type = None
    for session in check_list:
        if session['deviceType'].lower() == 'unknown':
            acct_unique_id = session['acct_unique_id']
            device_type = session['deviceType']
            break
        elif session['deviceType'].lower() == 'pc':
            acct_unique_id = session['acct_unique_id']
            device_type = session['deviceType']
    # 如果unknown和pc都不是的话,下线第一个
    # if acct_unique_id == None:
    #     acct_unique_id = check_list[0]['acct_unique_id']
    #     device_type = check_list[0]['deviceType']
    r = requests.delete(base_url+f'/acctUniqueId/{acct_unique_id}?noCache={int(time.time()*1000)}', headers=headers)
    if r.status_code == 200:
        print(f"已移除一台{device_type}设备")
        print("正在进行重新认证...")
        main(UID, PASSWD)
    else:
        print("认证错误")


# 可作为模块嵌入进其它程序
def main(uid, passwd):
    headers = {
        'Content-type': 'application/json',
        "User-Agent": "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
        }
    orig_url = 'http://s3.cn-northwest-1.amazonaws.com.cn/captive-portal/connection-test.html?noCache=%s'
    mac, ip = get_address()
    redirectUrl = f"http://10.16.0.12:8081/?usermac={mac}&userip={ip}&origurl={orig_url}&nasip=10.100.0.1"
    data = {
        "deviceType": "PC",
        "redirectUrl": redirectUrl%(int(time.time()*1000)),
        "type": "login",
        "webAuthPassword": passwd,
        "webAuthUser": uid,
        }
    r = requests.post(f'http://10.16.0.12:8081/portal/api/v2/online?noCache={int(time.time()*1000)}',headers=headers ,data=json.dumps(data))
    # print(r.text, '\n')


    headers["Authorization"] = json.loads(r.text)["token"]
    # 同时有两台设备已经在线
    error_code = json.loads(r.text).get("error")
    if error_code == 81:
        check_list(headers)
    else:
        print("认证成功")


if __name__ == '__main__':
    main(UID, PASSWD)
    input()
