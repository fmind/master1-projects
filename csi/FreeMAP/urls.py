from django.conf.urls.defaults import patterns, include, url
from settings import STATIC_ROOT, MEDIA_ROOT
from django.contrib import admin


admin.autodiscover()

urlpatterns = patterns('',
    url(r'^administration/', include(admin.site.urls)),
    url(r'^login$', 'django.contrib.auth.views.login', name='login'),
    url(r'^logout$', 'django.contrib.auth.views.logout', name='logout'),
    url(r'^propositions$', 'producteurs.views.propositions', name='propositions'),
    url(r'^propositions/nouvelle$', 'producteurs.views.nouvelle', name='nouvelle_proposition'),
    url(r'^abonnement$', 'utilisateurs.views.abonnement', name='abonnement'),
    url(r'^$', 'utilisateurs.views.index', name='index'),

    url(r'^static/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': STATIC_ROOT}),
    url(r'^media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': MEDIA_ROOT}),
)
