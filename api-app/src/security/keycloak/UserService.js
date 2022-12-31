import kc from '../../Keycloak';

const _kc = kc;

const store_token = (token) => {
    localStorage.setItem("keycloak",token);
}

/**
 * Initializes Keycloak instance and calls the provided callback function if successfully authenticated.
 *
 * @param onAuthenticatedCallback
 */
const initKeycloak = (onAuthenticatedCallback) => {

    _kc.init({
        checkLoginIframe: false,
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
        pkceMethod: 'S256',
    })
        .then((authenticated) => {
            if (!authenticated) {
                console.log("user is not authenticated..!");
            }

            if (onAuthenticatedCallback)
                onAuthenticatedCallback();

            setInterval(()=>{
                _kc.updateToken(70).then((refreshed)=>{
                    if (refreshed){
                        store_token(_kc.token);
                        console.info('Token Refreshed');
                    }else{
                        console.warn('Token not refreshed');
                    }
                }).catch(()=>{
                    console.error('Failed to refresh token');
                });
            },50000);

        })
        .catch(console.error);
};

const doLogin = _kc.login;

const doLogout = _kc.logout;

const getToken = () => _kc.token;

const isLoggedIn = () => !!_kc.token;

const updateToken = (successCallback) =>
    _kc.updateToken(5)
        .then(successCallback)
        .catch(doLogin);

const getUsername = () => _kc.tokenParsed?.preferred_username;

const hasRole = (roles) => roles.some((role) => _kc.hasRealmRole(role));

const UserService = {
    initKeycloak,
    doLogin,
    doLogout,
    isLoggedIn,
    getToken,
    updateToken,
    getUsername,
    hasRole,
};

export default UserService;