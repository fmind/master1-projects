# -*- coding: utf-8 -*-

from django.conf import global_settings
import os


DEBUG = True
TEMPLATE_DEBUG = DEBUG
THUMBNAIL_DEBUG = DEBUG

SITE_ID = 1
SECRET_KEY = 'k==k&8f9bf&czt-$z+t%+&x-l7adm1an-fjaa1)ultmtc(yh#b'

ADMINS = (
    ('Médéric HURIER', 'mederic.hurier@freaxmind.pro'),
    ('Omar EDDASSER', 'omar.eddasser@gmail.com'),
)
MANAGERS = ADMINS

DATABASE_ROUTERS = ['routers.DatabaseRouter',]

CACHES = {
    # python manage.py createcachetable cache
    'default': {
        'BACKEND': 'django.core.cache.backends.db.DatabaseCache',
        'LOCATION': 'cache',    
    }
}

EMAIL_HOST = 'smtp.alwaysdata.com'
EMAIL_PORT = 25

TIME_ZONE = 'Europe/Paris'
LANGUAGE_CODE = 'fr-fr'
USE_I18N = True
USE_L10N = True

SITE_ROOT = os.path.realpath(os.path.dirname(__file__))
MEDIA_ROOT = os.path.join(SITE_ROOT, 'public/media')
STATIC_ROOT = os.path.join(SITE_ROOT, 'public/static')

ROOT_URLCONF = 'urls'
MEDIA_URL = '/media/'
STATIC_URL = '/static/'

LOGIN_URL = '/login'
LOGOUT_URL = '/logout?next_page=/'

STATICFILES_DIRS = (

)
TEMPLATE_DIRS = (
    os.path.join(SITE_ROOT, 'gestion/templates'),
    os.path.join(SITE_ROOT, 'utilisateurs/templates'),
    os.path.join(SITE_ROOT, 'fournisseurs/templates'),
)

STATICFILES_FINDERS = (
    'django.contrib.staticfiles.finders.FileSystemFinder',
    'django.contrib.staticfiles.finders.AppDirectoriesFinder',
)
TEMPLATE_LOADERS = (
    'django.template.loaders.filesystem.Loader',
    'django.template.loaders.app_directories.Loader',
)
TEMPLATE_CONTEXT_PROCESSORS = global_settings.TEMPLATE_CONTEXT_PROCESSORS + (
    "django.core.context_processors.request",
)

MIDDLEWARE_CLASSES = (
    'django.middleware.common.CommonMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
)

INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.admin',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'django.contrib.contenttypes',
    'sorl.thumbnail',
    'gestion',
    'producteurs',
    'utilisateurs',
)

LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'handlers': {
        'mail_admins': {
            'level': 'ERROR',
            'class': 'django.utils.log.AdminEmailHandler'
        }
    },
    'loggers': {
        'django.request': {
            'handlers': ['mail_admins'],
            'level': 'ERROR',
            'propagate': True,
        },
    }
}
